package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    PlatformTransactionManager platformTransactionManagerAlbums;
    PlatformTransactionManager platformTransactionManagerMovies;

    // single TransactionTemplate shared amongst all methods in this instance
    private final TransactionTemplate transactionTemplateAlbums;
    private final TransactionTemplate transactionTemplateMovies;


    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures,
                          AlbumFixtures albumFixtures,PlatformTransactionManager getPlatformTransactionManagerAlbums,
                          PlatformTransactionManager getPlatformTransactionManagerMovies) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        this.platformTransactionManagerAlbums = getPlatformTransactionManagerAlbums;
        this.platformTransactionManagerMovies = getPlatformTransactionManagerMovies;
        this.transactionTemplateAlbums = new TransactionTemplate(getPlatformTransactionManagerAlbums);
        this.transactionTemplateMovies = new TransactionTemplate(getPlatformTransactionManagerMovies);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {


        addMovies();
        addAlbums();

        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }


    // manuel Transaction
    public Object addAlbums() {
        return transactionTemplateAlbums.execute(new TransactionCallbackWithoutResult() {

            // the code in this method executes in a transactional context
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (Album album : albumFixtures.load()) {
                    albumsBean.addAlbum(album);
                }
            }
        });
    }

    public Object addMovies() {
        return transactionTemplateMovies.execute(new TransactionCallbackWithoutResult() {

            // the code in this method executes in a transactional context
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for (Movie movie : movieFixtures.load()) {
                    moviesBean.addMovie(movie);
                }
            }
        });
    }
}
