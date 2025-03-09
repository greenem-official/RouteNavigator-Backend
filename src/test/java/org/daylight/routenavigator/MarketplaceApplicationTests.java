package org.daylight.routenavigator;

import org.daylight.routenavigator.backend.auth.HashManager;
import org.daylight.routenavigator.backend.entities.*;
import org.daylight.routenavigator.backend.model.incoming.RouteSearchRequest;
import org.daylight.routenavigator.backend.services.entityservices.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.system.OutputCaptureRule;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(classes = MarketplaceApplication.class, properties = {
		"spring.profiles.active=postgresql",
})
class MarketplaceApplicationTests {
//	private static final Logger logger = LoggerFactory.getLogger(MarketplaceApplicationTests.class);

	private static final String testDepartureTime = "2025-03-10T10:00:00+03:00";
	private static final Map<String, Boolean> testTransportTypes = Map.of(
			"transport_train", true,
			"transport_plane", true,
			"transport_bus", true);

	@Autowired
	private RouteService routeService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private TransportTypeService transportTypeService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

	@Test
	void contextLoads() {

	}

	@Test
	void testFindRoutes() {
		List<Route> result = routeService.findAllByRequest(new RouteSearchRequest()
				.setDepartureTimeMin(testDepartureTime)
				.setTransportAllowed(testTransportTypes)
				.setFetchDays(-1)
		);

		assertNotNull(result);
	}

	@Test
	void testFindRouteForDay() {
		List<Route> result = routeService.findAllByRequest(new RouteSearchRequest()
				.setDepartureTimeMin(testDepartureTime)
				.setTransportAllowed(testTransportTypes)
				.setFetchDays(1)
		);

		assertNotNull(result);
	}

	@Test
	void testFindRouteDates() {
		List<String> result = routeService.findDatesForRequest(new RouteSearchRequest()
				.setDepartureTimeMin(testDepartureTime)
				.setTransportAllowed(testTransportTypes)
				.setFetchDays(1)
				.setFetchAvailability(true)
		);

		assertNotNull(result);
	}

	@Test
	void testGetLocations() {
		String text = "";
		List<Location> result = locationService.findMatching(text, 100);

		assertNotNull(result);
	}

	@Test
	void testGetTransportTypes() {
		String text = "";
		List<TransportType> result = transportTypeService.findMatching(text, 100);

		assertNotNull(result);
	}

	@Test
	void testHashManager() {
		assertNotNull(HashManager.hashPassword("password"));
	}

	@Test
	void testUserCreationAndDeletion() {
		String username = "testUser_testUser";
		String email = "testUser_testUser@test.com";
		String password = "testUser_testUser";

		Optional<User> user = userService.findByUsername(username);
        user.ifPresent(value -> userService.delete(value));

		User testUser = new User()
				.setUsername(username)
				.setEmail(email)
				.setPasswordHash(HashManager.hashPassword(password));

		userService.save(testUser);

		Optional<User> userNew = userService.findByUsername(username);
		assertNotNull(userNew);

		userNew.ifPresent(value -> userService.delete(value));
	}

	@Test
	void testTokenOperations() {
		String username = "testUser_testUser";
		String email = "testUser_testUser@test.com";
		String password = "testUser_testUser";

		Optional<User> user = userService.findByUsername(username);

		if(user.isEmpty()) {
			User testUser = new User()
					.setUsername(username)
					.setEmail(email)
					.setPasswordHash(HashManager.hashPassword(password));
			userService.save(testUser);

			user = Optional.of(testUser);
		}

		Token token = tokenService.generateToken(user.get());
		assertNotNull(token);

		tokenService.save(token);
		tokenService.delete(token);

		user.ifPresent(value -> userService.delete(value));
	}
}
