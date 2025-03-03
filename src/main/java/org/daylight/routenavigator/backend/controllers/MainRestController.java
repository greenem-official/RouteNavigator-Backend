package org.daylight.routenavigator.backend.controllers;

import jakarta.validation.Valid;
import org.daylight.routenavigator.backend.auth.TokenManager;
import org.daylight.routenavigator.backend.entities.*;
import org.daylight.routenavigator.backend.model.incoming.*;
import org.daylight.routenavigator.backend.model.outcoming.ErrorResponse;
import org.daylight.routenavigator.backend.model.outcoming.MessageResponse;
import org.daylight.routenavigator.backend.model.outcoming.TokenResponse;
import org.daylight.routenavigator.backend.services.entitysaervices.*;
import org.daylight.routenavigator.backend.services.generalservices.Dijkstra3_2;
import org.daylight.routenavigator.constants.TimeContstants;
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
     * @return A new token info or an error
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
     * @return A new token info or an error
     */
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody LoginFormRequest registrationFormRequest) {
        if (registrationFormRequest.getUsername() == null) {
            return new ResponseEntity<>(new ErrorResponse("Username is required"), HttpStatus.BAD_REQUEST);
        }

        // Looking for a registered user with the same email
        Optional<User> user = userService.findByEmail(registrationFormRequest.getEmail());
        if (user.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("email_already_registered"));
        }

        // Looking for a registered user with the same email
        Optional<User> userByName = userService.findByUsername(registrationFormRequest.getUsername());
        if (userByName.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
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
     * @return The token info or an error
     */
    @GetMapping(value = "/checkToken", consumes = "application/json")
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

    @GetMapping(value = "/test")
    public ResponseEntity<?> test() {
//        System.out.println(Arrays.toString(routeService.findAllByDepartureLocation(locationService.findByCode("location_msk").get()).toArray()));
//        return new ResponseEntity<String>(userService.findUserFromToken())

        List<Route> routes = routeService.findAllByDepartureLocationAndDepartureTimeAfter(
                locationService.findByCode("rostov-on-don").get(),
                OffsetDateTime.parse("2025-03-22 07:00:00+03:00", TimeContstants.formatter)
        );

        System.out.println("Options:");
        for (Route route : routes) {
            System.out.println(route.getDepartureLocation().getDisplayName() + " - " + route.getArrivalLocation().getDisplayName());
        }

//        Dijkstra2 dijkstra = new Dijkstra2(routeService);
//
//        List<Path> shortestPaths = dijkstra.findShortestPaths(
//                locationService.findByCode("msk").get(),
//                locationService.findByCode("novosibirsk").get(),
//                OffsetDateTime.parse("2025-03-20 10:30:00+03:00", TimeContstants.formatter)
//        );
//
//        for (Path path : shortestPaths) {
//            System.out.println("Path with total time " + path.getTotalTime() + ": (" + path.getLocations().size() + " locations)");
//            for(Location location : path.getLocations()) {
//                System.out.print(location.getDisplayName() + " ");
//            }
//        }

//        System.out.println();

        Location startNode = locationService.findByCode("msk").get();
        Location endNode = locationService.findByCode("novosibirsk").get();
        OffsetDateTime startTime = OffsetDateTime.parse("2025-03-20 10:30:00+03:00", TimeContstants.formatter);
        int K = 5;

        Dijkstra3_2.setRouteService(routeService);
        List<Dijkstra3_2.Path> result = Dijkstra3_2.temporalDijkstraK(startNode, endNode, startTime, K);

        if (!result.isEmpty()) {
            System.out.println("Best paths to " + endNode + ":");
            for (Dijkstra3_2.Path path : result) {
                System.out.println(path);
                System.out.println();
            }
        } else {
            System.out.println("Path from " + startNode + " to " + endNode + " wasn't found.");
        }

        return new ResponseEntity<>(new MessageResponse("test"), HttpStatus.OK);
    }

    /**
     * An endpoint for querying matching routes
     * @param routeSearchRequest The route search request data
     * @return A list of available routes or an error
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

//        if (routeSearchRequest.isThisDateOnly()) {
//            // Single date request
//            routeSearchRequest.setDepartureTimeMax(routeSearchRequest.getDepartureTimeMin()
//                    .truncatedTo(ChronoUnit.DAYS)
//                    .plusDays(1)
//            );
//        } else {
////            // Limiting max search timespan
////            OffsetDateTime maxTechnicallyAllowedDepartureTime = routeSearchRequest.getDepartureTimeMin()
////                    .truncatedTo(ChronoUnit.DAYS)
////                    .plusDays(14);
////            if (maxTechnicallyAllowedDepartureTime.isBefore(routeSearchRequest.getDepartureTimeMax())) {
////                routeSearchRequest.setDepartureTimeMax(maxTechnicallyAllowedDepartureTime);
////            }
//        }

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

    @PostMapping(value = "/modifyBooking", consumes = "application/json")
    public ResponseEntity<?> modifyRouteBooking(@Valid @RequestBody ModifyRouteBookingRequest modifyRouteBookingRequest, @RequestHeader("Authorization") String authHeader) {
        Pair<Token, ResponseEntity<?>> tokenResult = TokenManager.findToken(authHeader);
        if(tokenResult.getValue1() != null) return tokenResult.getValue1();
        Token token = tokenResult.getValue0();

        Optional<Booking> booking = bookingService.findById(modifyRouteBookingRequest.getBookingId());
        if(booking.isEmpty()) return new ResponseEntity<>(new ErrorResponse("booking_not_found"), HttpStatus.NOT_FOUND);
        if(booking.get().getUser().getId() != token.getUser().getId()) return new ResponseEntity<>(new ErrorResponse("booking_not_available"), HttpStatus.UNAUTHORIZED);

        booking.get().setTicketAmount(modifyRouteBookingRequest.getSetTicketsAmount());
        bookingService.save(booking.get());

        return new ResponseEntity<>(new MessageResponse("booking_modification_success"), HttpStatus.OK);
    }

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
