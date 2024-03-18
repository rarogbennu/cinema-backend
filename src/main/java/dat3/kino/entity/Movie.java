package dat3.kino.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    @Column(length = 20000)
    private String plot;
    private String poster;
    private String metascore;
    private String imdbRating;
    private String imdbVotes;

    @Column(unique = true)
    private String imdbID;
    private String website;
    private String response;
}
