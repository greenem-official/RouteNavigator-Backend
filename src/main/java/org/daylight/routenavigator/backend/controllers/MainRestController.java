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

    @GetMapping(value = "/login", consumes = "application/json") // {thing}
    public ResponseEntity<?> login(@Valid @RequestBody LoginFormRequest loginFormRequest) { // @PathVariable String thing
        Optional<User> userIfCorrect = userService.findByEmailAndPassword(loginFormRequest.getEmail(), loginFormRequest.getPassword());
        if (userIfCorrect.isPresent()) {
            Token newToken = tokenService.generateToken(userIfCorrect.get().getId());
            return new ResponseEntity<>(new TokenResponse(newToken), HttpStatus.OK);
        } else {
            Optional<User> userIfRegistered = userService.findByEmail(loginFormRequest.getEmail());
            if (userIfRegistered.isPresent()) {
                return new ResponseEntity<>(new ErrorResponse("Wrong password"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ErrorResponse("Not registered"), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@Valid @RequestBody LoginFormRequest loginFormRequest) {
        Optional<User> user = userService.findByEmailAndPassword(loginFormRequest.getEmail(), loginFormRequest.getPassword());
        if (user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Already registered"), HttpStatus.OK);
        } else {
            User newUser = new User()
                    .setEmail(loginFormRequest.getEmail())
                    .setPassword(loginFormRequest.getPassword());
            userService.save(newUser);

            Token newToken = tokenService.generateToken(newUser.getId());
            return new ResponseEntity<>(new TokenResponse(newToken), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/checkToken", consumes = "application/json")
    public ResponseEntity<?> checkToken(@Valid @RequestBody TokenCheckRequest tokenCheckRequest) {
        UUID uuid;
        try {
            uuid = UUID.fromString(tokenCheckRequest.getToken());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("Invalid token"), HttpStatus.BAD_REQUEST);
        }
        Optional<Token> token = tokenService.findByToken(uuid);
        if (token.isPresent()) {
            boolean active = tokenService.checkTokenActive(token.get());
            if (!active) {
                return new ResponseEntity<>(new MessageResponse("Token expired"), HttpStatus.OK);
            }
            else {
                // Success
                return new ResponseEntity<>(new TokenResponse(token.get()), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Token not found"), HttpStatus.OK);
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

    // Move to somewhere else
    private final List<String> possibleTransportTypes = Arrays.asList("transport_plane", "transport_train", "transport_bus");

    @PostMapping(value = "/findRoutes", consumes = "application/json")
    public ResponseEntity<?> findRoutes(@RequestBody RouteSearchRequest routeSearchRequest) {
        System.out.println(routeSearchRequest.toString());
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

    @GetMapping(value = "/getLocations")
    public ResponseEntity<?> getLocations(@Valid @RequestParam String text) {
        return new ResponseEntity<>(locationService.findMatching(text.trim(), 100), HttpStatus.OK);
    }

    @GetMapping(value = "/getTransportTypes")
    public ResponseEntity<?> getTransportTypes(@Valid @RequestParam String text) {
        return new ResponseEntity<>(transportTypeService.findMatching(text.trim(), 100), HttpStatus.OK);
    }
}
