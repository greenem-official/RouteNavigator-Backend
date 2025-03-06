package org.daylight.routenavigator.backend.controllers;

import jakarta.validation.Valid;
import org.daylight.routenavigator.backend.auth.TokenManager;
import org.daylight.routenavigator.backend.entities.*;
import org.daylight.routenavigator.backend.model.incoming.*;
import org.daylight.routenavigator.backend.model.outcoming.ErrorResponse;
import org.daylight.routenavigator.backend.model.outcoming.MessageResponse;
import org.daylight.routenavigator.backend.model.outcoming.TokenResponse;
import org.daylight.routenavigator.backend.services.entitysaervices.*;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * The main rest controller for the backend
 */
@RestController
@RequestMapping("/api/main")
//@CrossOrigin(origins = "*")
public class MainRestController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private LocationService locationService;
    @Autowired
    private TransportTypeService transportTypeService;

    /**
     * A login endpoint
     * @param loginFormRequest The login form data
     * @return A new token info or an ErrorResponse
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@Valid @RequestBody LoginFormRequest loginFormRequest) {
        // Looking for a matching registered user
        Optional<User> userIfCorrect = userService.findByEmailAndPassword(loginFormRequest.getEmail(), loginFormRequest.getPassword());

        // If email and password didn't match any account
        if (userIfCorrect.isEmpty()) {
            Optional<User> userIfRegistered = userService.findByEmail(loginFormRequest.getEmail());

            // The provided password is wrong
            if (userIfRegistered.isPresent()) return new ResponseEntity<>(new ErrorResponse("wrong_password"), HttpStatus.OK);

            // The user is not even registered
            return new ResponseEntity<>(new ErrorResponse("user_not_found"), HttpStatus.OK);
        }

        // Generating a token and sending the result
        Optional<Token> newToken = tokenService.generateToken(userIfCorrect.get());
        if (newToken.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("invalid_token"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new TokenResponse(newToken.get())
                .setUserUsername(userIfCorrect.get().getUsername())
                .setUserEmail(userIfCorrect.get().getEmail())
                , HttpStatus.OK);
    }

    /**
     * A registration endpoint
     * @param registrationFormRequest The registration form data
     * @return A new token info or an ErrorResponse
     */
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody LoginFormRequest registrationFormRequest) {
        if (registrationFormRequest.getUsername() == null) {
            return new ResponseEntity<>(new ErrorResponse("Username is required"), HttpStatus.BAD_REQUEST);
        }

        // Looking for a registered user with the same email
        Optional<User> user = userService.findByEmail(registrationFormRequest.getEmail());
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse("email_already_registered"));
        }

        // Looking for a registered user with the same email
        Optional<User> userByName = userService.findByUsername(registrationFormRequest.getUsername());
        if (userByName.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ErrorResponse("username_already_registered"));
        }

        // Registering the user
        User newUser = new User()
                .setEmail(registrationFormRequest.getEmail())
                .setUsername(registrationFormRequest.getUsername())
                .setPassword(registrationFormRequest.getPassword());

        try {
            userService.save(newUser);
        } catch (DataIntegrityViolationException e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse("internal_error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Generating a token and sending the result
        Optional<Token> newToken = tokenService.generateToken(newUser);
        if (newToken.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("invalid_token"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new TokenResponse(newToken.get())
                .setUserUsername(newUser.getUsername())
                .setUserEmail(newUser.getEmail())
                , HttpStatus.OK);
    }

    /**
     * An endpoint for checking the token and getting user information
     * @param tokenCheckRequest The request data
     * @return The token info or an ErrorResponse
     */
    @PostMapping(value = "/checkToken", consumes = "application/json")
    public ResponseEntity<?> checkToken(@Valid @RequestBody TokenCheckRequest tokenCheckRequest) {
        Pair<Token, ResponseEntity<?>> tokenResult = TokenManager.findToken(tokenCheckRequest.getToken());
        if (tokenResult.getValue1() != null) return tokenResult.getValue1();
        else {
            Token token = tokenResult.getValue0();
            return new ResponseEntity<>(new TokenResponse(token)
                    .setUserUsername(token.getUser().getUsername())
                    .setUserEmail(token.getUser().getEmail())
                    , HttpStatus.OK);
        }
    }

    /**
     * An endpoint for querying matching routes
     * @param routeSearchRequest The route search request data
     * @return A list of available routes or an ErrorResponse
     */
    @PostMapping(value = "/findRoutes", consumes = "application/json")
    public ResponseEntity<?> findRoutes(@Valid @RequestBody RouteSearchRequest routeSearchRequest) {
//        System.out.println(routeSearchRequest.toString());
        try {
            OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin());
            OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMax());
        } catch (NullPointerException | DateTimeParseException e) {
            return new ResponseEntity<>(new ErrorResponse("invalid_time_format"), HttpStatus.BAD_REQUEST);
        }

        if(routeSearchRequest.getFetchDays() > 0 && routeSearchRequest.isFetchAvailability()) {
            List<String> dates = routeService.findDatesForRequest(routeSearchRequest);
            return new ResponseEntity<>(dates, HttpStatus.OK);
        }
        try {
            List<Route> routes = routeService.findAllByRequest(routeSearchRequest);
            return new ResponseEntity<>(routes, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * An endpoint for booking a specific route
     * @param bookRouteRequest Specific request data
     * @param authHeader Auth data
     * @return A MessageResponse or an ErrorResponse
     */
    @PostMapping(value = "/bookRoute", consumes = "application/json")
    public ResponseEntity<?> bookRoute(@Valid @RequestBody BookRouteRequest bookRouteRequest, @RequestHeader("Authorization") String authHeader) {
        Pair<Token, ResponseEntity<?>> tokenResult = TokenManager.findToken(authHeader);
        if(tokenResult.getValue1() != null) return tokenResult.getValue1();
        Token token = tokenResult.getValue0();

        Optional<Route> route = routeService.findById(bookRouteRequest.getRouteId());
        if(route.isEmpty()) return new ResponseEntity<>(new ErrorResponse("route_not_found"), HttpStatus.NOT_FOUND);

        Booking booking = new Booking()
                .setRoute(route.get())
                .setUser(token.getUser())
                .setBookedAt(OffsetDateTime.now())
                .setTicketAmount(bookRouteRequest.getTicketsAmount());
        bookingService.save(booking);

        return new ResponseEntity<>(new MessageResponse("route_booking_success"), HttpStatus.OK);
    }

    /**
     * An endpoint for a booking modification request
     * @param modifyRouteBookingRequest Specific request data
     * @param authHeader Auth data
     * @return A MessageResponse or an ErrorResponse
     */
    @PostMapping(value = "/modifyBooking", consumes = "application/json")
    public ResponseEntity<?> modifyRouteBooking(@Valid @RequestBody ModifyRouteBookingRequest modifyRouteBookingRequest, @RequestHeader("Authorization") String authHeader) {
        Pair<Token, ResponseEntity<?>> tokenResult = TokenManager.findToken(authHeader);
        if(tokenResult.getValue1() != null) return tokenResult.getValue1();
        Token token = tokenResult.getValue0();

        Optional<Booking> booking = bookingService.findById(modifyRouteBookingRequest.getBookingId());
        if(booking.isEmpty()) return new ResponseEntity<>(new ErrorResponse("booking_not_found"), HttpStatus.NOT_FOUND);
        if(booking.get().getUser().getId() != token.getUser().getId()) return new ResponseEntity<>(new ErrorResponse("booking_not_available"), HttpStatus.UNAUTHORIZED);

        booking.get().setTicketAmount(modifyRouteBookingRequest.getSetTicketsAmount());
        if(booking.get().getTicketAmount() == 0) {
            bookingService.delete(booking.get());
        } else {
            bookingService.save(booking.get());
        }

        return new ResponseEntity<>(new MessageResponse("booking_modification_success"), HttpStatus.OK);
    }

    /**
     * An endpoint for querying all the user's bookings
     * @param authHeader Auth data
     * @return A list of bookings
     */
    @GetMapping(value = "/getBookings")
    public ResponseEntity<?> getAllBookings(@RequestHeader("Authorization") String authHeader) {
        Pair<Token, ResponseEntity<?>> tokenResult = TokenManager.findToken(authHeader);
        if(tokenResult.getValue1() != null) return tokenResult.getValue1();
        Token token = tokenResult.getValue0();

        List<Booking> bookings = bookingService.findAllByUser(token.getUser());

        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    /**
     * An endpoint for querying a list of locations matching a text
     * @param text The beginning of the location's display name
     * @return A list of matching locations
     */
    @GetMapping(value = "/getLocations")
    public ResponseEntity<?> getLocations(@Valid @RequestParam String text) {
        return new ResponseEntity<>(locationService.findMatching(text.trim(), 100), HttpStatus.OK);
    }

    /**
     * An endpoint for querying a list of transport types matching a text
     * @param text The beginning of the transport type's display name
     * @return A list of matching transport types
     */
    @GetMapping(value = "/getTransportTypes")
    public ResponseEntity<?> getTransportTypes(@Valid @RequestParam String text) {
        return new ResponseEntity<>(transportTypeService.findMatching(text.trim(), 100), HttpStatus.OK);
    }
}
