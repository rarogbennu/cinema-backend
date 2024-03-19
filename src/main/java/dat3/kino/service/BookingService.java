package dat3.kino.service;

import dat3.kino.dto.BookingDTO;
import dat3.kino.entity.Reservation;
import dat3.kino.entity.Booking;
import dat3.kino.repository.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(int id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, " Booking not found"));
        return new BookingDTO(booking, false);
    }


    public BookingDTO createBooking(List<Reservation> reservations) {
        // Calculate the total price by summing up the prices of individual reservations
        double totalPrice = reservations.stream()
                .mapToDouble(Reservation::getPrice)
                .sum();

        // Apply discounts based on the total number of reservations
        int orderSize = reservations.size();
        if (orderSize >= 6 && orderSize <= 10) {
            // No change in price
        } else if (orderSize >= 11) {
            totalPrice *= 0.95; // 5% discount for orders of 11 or more seats
        } else {
            totalPrice *= 1.05; // 5% additional charge for orders of 1-5 seats
        }

        // Log totalPrice before saving
        System.out.println("Total Price Before Discount: " + totalPrice);

        // Create and save  Booking
        Booking booking = new Booking();
        booking.setReservations(reservations);
        booking.setTotalPrice(totalPrice);

        // Save  Booking
        Booking savedBooking = bookingRepository.save(booking);

        // Log saved Booking before updating total price
        System.out.println(" Booking Before Update: " + savedBooking);

        // Update total price in  Booking entity with the discounted price
        savedBooking.setTotalPrice(totalPrice);
        savedBooking = bookingRepository.save(savedBooking);

        // Log saved Booking after updating total price
        System.out.println(" Booking After Update: " + savedBooking);

        return convertToDTO(savedBooking);
    }






    public void deleteBooking(int id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    // Convert entity to DTO
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        // Set other fields as needed
        return dto;
    }

    // Get List of DTOs
    private List<BookingDTO> convertToDTOs(List<Booking> bookings) {
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }




}
