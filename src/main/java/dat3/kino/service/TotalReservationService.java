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

@Service
public class TotalReservationService {

    private final TotalReservationRepository totalReservationRepository;

    public TotalReservationService(TotalReservationRepository totalReservationRepository) {
        this.totalReservationRepository = totalReservationRepository;
    }

    public List<TotalReservationDTO> getAllTotalReservations() {
        List<TotalReservation> totalReservations = totalReservationRepository.findAll();
        return totalReservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TotalReservationDTO getTotalReservationById(int id) {
        TotalReservation totalReservation = totalReservationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "TotalReservation not found"));
        return new TotalReservationDTO(totalReservation, false);
    }


    public TotalReservationDTO createTotalReservation(List<Reservation> reservations) {
        TotalReservation totalReservation = new TotalReservation();
        totalReservation.getReservations().addAll(reservations); // Reservation to TotalReservation
        TotalReservation savedTotalReservation = totalReservationRepository.save(totalReservation);
        return convertToDTO(savedTotalReservation);
    }



    public void deleteTotalReservation(int id) {
        if (!totalReservationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TotalReservation not found");
        }
        totalReservationRepository.deleteById(id);
    }

    // Convert entity to DTO
    private TotalReservationDTO convertToDTO(TotalReservation totalReservation) {
        TotalReservationDTO dto = new TotalReservationDTO();
        dto.setId(totalReservation.getId());
        // Set other fields as needed
        return dto;
    }

    // Get List of DTOs
    private List<TotalReservationDTO> convertToDTOs(List<TotalReservation> totalReservations) {
        return totalReservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}

