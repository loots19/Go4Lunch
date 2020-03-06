package com.e.go4lunch.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.go4lunch.R;
import com.e.go4lunch.WorkmatesAdapter;
import com.e.go4lunch.models.Workmates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<Workmates> mWorkmates = Arrays.asList(
            new Workmates("https://cache.marieclaire.fr/data/photo/w1000_ci/4y/gilles-lellouche3.jpg","Gilles"),
            new Workmates("https://mcetv.fr/wp-content/uploads/2017/10/Vikings-saison-6-Une-actrice-confirme-le-retour-de-Lagertha-big.jpg","Lagertha"),
            new Workmates("https://cdn-s-www.ledauphine.com/images/F5C41CA1-DE03-480C-AAC6-230CFC827DA7/NW_raw/emilia-clarke-incarne-daenerys-targaryen-dans-game-of-thrones-photo-hbo-1553199730.jpg","Daenerys"),
            new Workmates("https://static.lexpress.fr/medias_8012/w_1000,h_435,c_crop,x_0,y_45/w_480,h_270,c_fill,g_north/v1389884413/morgan-freeman-2_4102614.jpg","Morgan"),
            new Workmates("https://www.premiere.fr/sites/default/files/styles/scale_crop_560x800/public/2018-04/alain-chabat.jpg","Alain"),
            new Workmates("https://fr.web.img3.acsta.net/r_1280_720/newsv7/16/02/19/15/45/1575830.jpg","Leonardo"),
            new Workmates("https://p7.storage.canalblog.com/71/11/902825/75209274.jpg","Lucie"),
            new Workmates("https://i.skyrock.net/7317/53357317/pics/2152860685_small_1.jpg","Big"),
            new Workmates("https://adra-matic.com/wp-content/uploads/2011/12/RZA1.jpg","RZA"),
            new Workmates("https://media2.fdncms.com/pittsburgh/imager/the-tao-of-the-steubenville-public-library-rza/u/original/1637803/music1storymain_web.jpg","rza")

    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this,view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        WorkmatesAdapter adapter = new WorkmatesAdapter(this.getActivity(),mWorkmates);
        mRecyclerView.setAdapter(adapter);
        return view;

    }

}
