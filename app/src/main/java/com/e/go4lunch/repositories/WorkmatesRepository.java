package com.e.go4lunch.repositories;

import androidx.lifecycle.MutableLiveData;

import com.e.go4lunch.models.Workmates;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesRepository {

    private static WorkmatesRepository instance;
    private ArrayList<Workmates> dataSet = new ArrayList<>();

    public static WorkmatesRepository getInstance(){
        if(instance == null){
            instance = new WorkmatesRepository();
        }
        return instance;
    }
    public MutableLiveData<List<Workmates>> getWorkmates(){
     setWorkmates();

     MutableLiveData<List<Workmates>> data = new MutableLiveData<>();
     data.setValue(dataSet);
     return data;
    }
    private void setWorkmates(){
        dataSet.add(
                new Workmates("","Gilles","gilles@gmail.com","https://cache.marieclaire.fr/data/photo/w1000_ci/4y/gilles-lellouche3.jpg")
        );
        dataSet.add(
                new Workmates("","Lagertha","lagertha@gmail.com","https://mcetv.fr/wp-content/uploads/2017/10/Vikings-saison-6-Une-actrice-confirme-le-retour-de-Lagertha-big.jpg")
        );
        dataSet.add(
                new Workmates("","Daenerys","danerys@gmail.com","https://cdn-s-www.ledauphine.com/images/F5C41CA1-DE03-480C-AAC6-230CFC827DA7/NW_raw/emilia-clarke-incarne-daenerys-targaryen-dans-game-of-thrones-photo-hbo-1553199730.jpg")
        );
        dataSet.add(
                new Workmates("","Morgan","morgan@gmail.com","https://static.lexpress.fr/medias_8012/w_1000,h_435,c_crop,x_0,y_45/w_480,h_270,c_fill,g_north/v1389884413/morgan-freeman-2_4102614.jpg")
        );
        dataSet.add(
                new Workmates("","Alain","alainn@gmail.com","https://www.premiere.fr/sites/default/files/styles/scale_crop_560x800/public/2018-04/alain-chabat.jpg")
        );
        dataSet.add(
                new Workmates("","Leonardo","leonardo@gmail.com","https://fr.web.img3.acsta.net/r_1280_720/newsv7/16/02/19/15/45/1575830.jpg")
        );
        dataSet.add(
                new Workmates("","Lucie","lucie@gmail.com","https://p7.storage.canalblog.com/71/11/902825/75209274.jpg")
        );
        dataSet.add(
                new Workmates("","Big","big@gmail.com","https://i.skyrock.net/7317/53357317/pics/2152860685_small_1.jpg")
        );



    }



}
