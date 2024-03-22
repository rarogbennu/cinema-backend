package dat3.kino.service;

import dat3.kino.dto.TotalReservationDTO;
import dat3.kino.entity.Reservation;
import dat3.kino.entity.TotalReservation;
import dat3.kino.repository.TotalReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling operations related to TotalReservation entities.
 */
@Service
public class TotalReservationService {

    private final TotalReservationRepository totalReservationRepository;

    /**
     * Constructs a TotalReservationService with the provided TotalReservationRepository.
     *
     * @param totalReservationRepository The repository for TotalReservation entities.
     */
    public TotalReservationService(TotalReservationRepository totalReservationRepository) {
        this.totalReservationRepository = totalReservationRepository;
    }

    /**
     * Retrieves all total reservations from the database.
     *
     * @return List of DTOs representing total reservations.
     */
    public List<TotalReservationDTO> getAllTotalReservations() {
        List<TotalReservation> totalReservations = totalReservationRepository.findAll();
        return totalReservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a total reservation by its ID.
     *
     * @param id The ID of the total reservation to retrieve.
     * @return The DTO representing the total reservation.
     */
    public TotalReservationDTO getTotalReservationById(int id) {
        TotalReservation totalReservation = totalReservationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "TotalReservation not found"));
        return new TotalReservationDTO(totalReservation, false);
    }


//    public TotalReservationDTO createTotalReservation(List<Reservation> reservations) {
//        // Calculate the total price by summing up the prices of individual reservations
//        double totalPrice = reservations.stream()
//                .mapToDouble(Reservation::getPrice)
//                .sum();
//
//        // Apply discounts based on the total number of reservations
//        int orderSize = reservations.size();
//        if (orderSize >= 6 && orderSize <= 10) {
//            // No change in price
//        } else if (orderSize >= 11) {
//            totalPrice *= 0.95; // 5% discount for orders of 11 or more seats
//        } else {
//            totalPrice *= 1.05; // 5% additional charge for orders of 1-5 seats
//        }
//
//        // Log totalPrice before saving
//        System.out.println("Total Price Before Discount: " + totalPrice);
//
//        // Create and save TotalReservation
//        TotalReservation totalReservation = new TotalReservation();
//        totalReservation.setReservations(reservations);
//        totalReservation.setTotalPrice(totalPrice);
//
//        // Save TotalReservation
//        TotalReservation savedTotalReservation = totalReservationRepository.save(totalReservation);
//
//        // Log savedTotalReservation before updating total price
//        System.out.println("TotalReservation Before Update: " + savedTotalReservation);
//
//        // Update total price in TotalReservation entity with the discounted price
//        savedTotalReservation.setTotalPrice(totalPrice);
//        savedTotalReservation = totalReservationRepository.save(savedTotalReservation);
//
//        // Log savedTotalReservation after updating total price
//        System.out.println("TotalReservation After Update: " + savedTotalReservation);
//
//        return convertToDTO(savedTotalReservation);
//    }

    /**
     * Creates a total reservation based on a list of individual reservations.
     *
     * @param reservations List of reservations to be combined into a total reservation.
     * @return The DTO representing the newly created total reservation.
     */
    public TotalReservationDTO createTotalReservation(List<Reservation> reservations) {
        // Create or retrieve an existing TotalReservation
        TotalReservation totalReservation = reservations.get(0).getTotalReservation();
        if (totalReservation == null) {
            totalReservation = new TotalReservation();
        }

        // Add all reservations to the same TotalReservation
        for (Reservation reservation : reservations) {
            totalReservation.addReservation(reservation);
        }

        // Calculate the total price for the total reservation
        totalReservation.calculateTotalPrice();

        // Save the TotalReservation
        TotalReservation savedTotalReservation = totalReservationRepository.save(totalReservation);

        // Convert and return the saved TotalReservationDTO
        return convertToDTO(savedTotalReservation);
    }



    /**
     * Deletes a total reservation by its ID.
     *
     * @param id The ID of the total reservation to delete.
     */
    public void deleteTotalReservation(int id) {
        if (!totalReservationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TotalReservation not found");
        }
        totalReservationRepository.deleteById(id);
    }

    // Convert entity to DTO
    /**
     * Converts a TotalReservation entity to its corresponding DTO representation.
     *
     * @param totalReservation The TotalReservation entity to be converted.
     * @return TotalReservationDTO The DTO representation of the TotalReservation entity.
     */
    private TotalReservationDTO convertToDTO(TotalReservation totalReservation) {
        TotalReservationDTO dto = new TotalReservationDTO();
        dto.setId(totalReservation.getId());
        dto.setTotalPrice(totalReservation.getTotalPrice());
        // Set other fields as needed
        return dto;
    }

    /**
     * Converts a list of TotalReservation entities to their corresponding DTO representations.
     *
     * @param totalReservations The list of TotalReservation entities to be converted.
     * @return List<TotalReservationDTO> The list of DTO representations of TotalReservation entities.
     */
    private List<TotalReservationDTO> convertToDTOs(List<TotalReservation> totalReservations) {
        return totalReservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


}
