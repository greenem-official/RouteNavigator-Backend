package org.daylight.routenavigator.backend.controllers;

import jakarta.validation.Valid;
import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.entities.Token;
import org.daylight.routenavigator.backend.entities.User;
import org.daylight.routenavigator.backend.model.incoming.RouteSearchRequest;
import org.daylight.routenavigator.backend.model.incoming.TextSearchRequest;
import org.daylight.routenavigator.backend.model.incoming.TokenCheckRequest;
import org.daylight.routenavigator.backend.model.outcoming.ErrorResponse;
import org.daylight.routenavigator.backend.model.incoming.LoginFormRequest;
import org.daylight.routenavigator.backend.model.outcoming.MessageResponse;
import org.daylight.routenavigator.backend.model.outcoming.TokenResponse;
import org.daylight.routenavigator.backend.services.entitysaervices.*;
import org.daylight.routenavigator.backend.services.generalservices.Dijkstra3_2;
import org.daylight.routenavigator.constants.TimeContstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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
            if (userIfRegistered.isPresent()) return new ResponseEntity<>(new ErrorResponse("Wrong password"), HttpStatus.OK);

            // The user is not even registered
            return new ResponseEntity<>(new ErrorResponse("Not registered"), HttpStatus.OK);
        }

        // Generating a token and sending the result
        Token newToken = tokenService.generateToken(userIfCorrect.get().getId());
        return new ResponseEntity<>(new TokenResponse(newToken)
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

        // Checking that the user is not registered already
        Optional<User> user = userService.findByEmailAndPassword(registrationFormRequest.getEmail(), registrationFormRequest.getPassword());
        if (user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Already registered"), HttpStatus.OK);
        }

        // Registering the user
        User newUser = new User()
                .setUsername(registrationFormRequest.getUsername())
                .setEmail(registrationFormRequest.getEmail())
                .setPassword(registrationFormRequest.getPassword());
        userService.save(newUser);

        // Generating a token and sending the result
        Token newToken = tokenService.generateToken(newUser.getId());
        return new ResponseEntity<>(new TokenResponse(newToken), HttpStatus.OK);
    }

    /**
     * An endpoint for checking the token and getting user information
     * @param tokenCheckRequest The request data
     * @return The token info or an error
     */
    @GetMapping(value = "/checkToken", consumes = "application/json")
    public ResponseEntity<?> checkToken(@Valid @RequestBody TokenCheckRequest tokenCheckRequest) {
        // Parsing the UUID object
        UUID uuid;
        try {
            uuid = UUID.fromString(tokenCheckRequest.getToken());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("Invalid token"), HttpStatus.BAD_REQUEST);
        }

        // Checking if token is real
        Optional<Token> token = tokenService.findByToken(uuid);
        if (token.isEmpty()) {
            return new ResponseEntity<>(new MessageResponse("Token not found"), HttpStatus.OK);
        }

        // Checking if token is not expired
        boolean active = tokenService.checkTokenActive(token.get());
        if (!active) {
            return new ResponseEntity<>(new MessageResponse("Token expired"), HttpStatus.OK);
        }

        // Finding the user
        Optional<User> user = userService.findById(token.get().getUserId());
        if (user.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Sending the result
        return new ResponseEntity<>(new TokenResponse(token.get())
                .setUserUsername(user.get().getUsername())
                .setUserEmail(user.get().getEmail())
                , HttpStatus.OK);
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
    public ResponseEntity<?> findRoutes(@RequestBody RouteSearchRequest routeSearchRequest) {
//        System.out.println(routeSearchRequest.toString());
        try {
            OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin());
            OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMax());
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(new ErrorResponse("Invalid time format"), HttpStatus.BAD_REQUEST);
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
        try {
            List<Route> routes = routeService.findAllByRequest(routeSearchRequest);
            return new ResponseEntity<>(routes, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
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
