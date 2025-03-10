package org.daylight.routenavigator;

import org.daylight.routenavigator.backend.auth.HashManager;
import org.daylight.routenavigator.backend.entities.*;
import org.daylight.routenavigator.backend.model.incoming.RouteSearchRequest;
import org.daylight.routenavigator.backend.services.entityservices.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = MarketplaceApplication.class, properties = {
		"spring.profiles.active=postgresql",
})
class MarketplaceApplicationTests {
	private static final String testDepartureTime = "2025-03-10T10:00:00+03:00";
	private static final List<String> testTransportTypes = Arrays.asList(
			"transport_train",
			"transport_plane",
			"transport_bus"
	);

	private static final String testUserUsername = "testUser_testUser";
	private static final String testUserEmail = "testUser_testUser@test.com";
	private static final String testUserPassword = "testUser_testUser";

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
    @Autowired
    private BookingService bookingService;

	@Test
	void contextLoads() { }

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
	void testRouteBooking() {
		int ticketAmount = 10;

		Optional<Route> route = routeService.findAllByRequest(new RouteSearchRequest()
				.setDepartureTimeMin(testDepartureTime)
				.setTransportAllowed(testTransportTypes)
				.setFetchDays(-1)
		).stream().findFirst();

		if(route.isEmpty()) return; // no route data in the database

		// Creating a booking
		Booking booking = new Booking()
				.setUser(createTestUser())
				.setRoute(route.get())
				.setTicketAmount(ticketAmount)
				.setBookedAt(OffsetDateTime.now());

		bookingService.save(booking);

		// Checking that it's being found
		List<Booking> bookingsByUser = bookingService.findAllByUser(createTestUser());
		List<Booking> bookingsByRoute = bookingService.findAllByRoute(route.get());

		assertNotNull(bookingsByUser);
		assertNotNull(bookingsByRoute);

		assert !bookingsByUser.isEmpty();
		assert !bookingsByRoute.isEmpty();

		bookingService.delete(bookingsByUser.getFirst());

		deleteTestUser();
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

	private Optional<User> findTestUser() {
		return userService.findByUsername(testUserUsername);
	}

	@SuppressWarnings("UnusedReturnValue")
	private boolean deleteTestUser() {
		Optional<User> user = findTestUser();
		boolean existed = user.isPresent();

        user.ifPresent(value -> userService.delete(value));
		return existed;
	}

	@SuppressWarnings("UnusedReturnValue")
	private User createTestUser() {
		Optional<User> existingUser = findTestUser();
		if(existingUser.isPresent()) return existingUser.get();

		User testUser = new User()
				.setUsername(testUserUsername)
				.setEmail(testUserEmail)
				.setPasswordHash(HashManager.hashPassword(testUserPassword));

		userService.save(testUser);
		return testUser;
	}

	@Test
	void testUserCreationAndDeletion() {
		deleteTestUser();

		assert findTestUser().isEmpty();

		createTestUser();

		assert findTestUser().isPresent();

		deleteTestUser();
	}

	@Test
	void testTokenOperations() {
		User user = createTestUser();

		Token token = tokenService.generateToken(user);
		assertNotNull(token);

		tokenService.save(token);
		tokenService.delete(token);

		deleteTestUser();
	}
}
