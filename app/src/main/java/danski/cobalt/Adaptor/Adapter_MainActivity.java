package danski.cobalt.Adaptor;


import com.yqritc.recyclerviewmultipleviewtypesadapter.ListBindAdapter;



/**
 * Created by danny on 24/09/2015.
 */


public class Adapter_MainActivity extends ListBindAdapter{



    public Adapter_MainActivity(){
        addAllBinder(new Binder_lastmatch(this), new Binder_maingraphs(this));
    }


}
