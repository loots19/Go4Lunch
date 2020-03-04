package com.e.go4lunch.service;

import com.e.go4lunch.models.Workmates;

import java.util.Arrays;
import java.util.List;

public abstract class DummyWormatesGenerator {
    public static List<Workmates> DUMMY_WORKMATES = Arrays.asList(
            new Workmates("https://cache.marieclaire.fr/data/photo/w1000_ci/4y/gilles-lellouche3.jpg","Gilles"),
            new Workmates("https://mcetv.fr/wp-content/uploads/2017/10/Vikings-saison-6-Une-actrice-confirme-le-retour-de-Lagertha-big.jpg","Lagertha"),
            new Workmates("https://cdn-s-www.ledauphine.com/images/F5C41CA1-DE03-480C-AAC6-230CFC827DA7/NW_raw/emilia-clarke-incarne-daenerys-targaryen-dans-game-of-thrones-photo-hbo-1553199730.jpg","Daenerys"),
            new Workmates("https://static.lexpress.fr/medias_8012/w_1000,h_435,c_crop,x_0,y_45/w_480,h_270,c_fill,g_north/v1389884413/morgan-freeman-2_4102614.jpg","Morgan"),
            new Workmates("https://www.premiere.fr/sites/default/files/styles/scale_crop_560x800/public/2018-04/alain-chabat.jpg","Alain"),
            new Workmates("https://fr.web.img3.acsta.net/r_1280_720/newsv7/16/02/19/15/45/1575830.jpg","Leonardo"),
            new Workmates("https://p7.storage.canalblog.com/71/11/902825/75209274.jpg","Lucie")
    );
}
