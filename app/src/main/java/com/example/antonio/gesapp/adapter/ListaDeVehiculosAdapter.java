package com.example.antonio.gesapp.adapter;

// Esta clase define el acomodo de los datos de la lista de vehiculos "adaptador"
// dentro de esta creamos el viewholder

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.antonio.gesapp.Pojos.Conductor;
import com.example.antonio.gesapp.R;

import java.util.ArrayList;
import java.util.List;

/**/
// 1.- crear la clase   4.- exterder de RecyclerView.Adapter dentro de los corchetes lleva el ViewHolder (com.example.antonio..) definido en el numero 2 se pone en rojo e implementamos los metodos
public class ListaDeVehiculosAdapter extends RecyclerView.Adapter<ListaDeVehiculosAdapter.ViewHolder> implements Filterable {

    Context context;

    private List<Conductor>conductorList;

    //  1 .- se implementa para el filtrado
    private List<Conductor>conductorListfull;

    // definimos construtor
    public ListaDeVehiculosAdapter(Context context, List<Conductor> conductorList){

        this.context = context;
        this.conductorList = conductorList;
        // 2.- se implementa para el filtrado
        conductorListfull = new ArrayList<>(conductorList);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // vista que se va a inflar (archivo xml) el reurso es del layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardconductor,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // con este metodo definimos la posicion de los elementos declarados en public clase View.Holder extends Recycler.View
    // y se define el contenido de tendre cada vista
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textotitulo3placa.setText(conductorList.get(position).getPlaca());
    }

    // retorna el tamaño de la lista
    @Override
    public int getItemCount() {
        return conductorList.size();
    }

    @Override
    public Filter getFilter() {

        return conductorListFilter;
    }
    private Filter conductorListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Conductor> filteredList= new ArrayList<>();

            if(charSequence == null || charSequence.length()== 0){

                filteredList.addAll(conductorListfull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Conductor item: conductorListfull){
                    if(item.getPlaca().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {

            conductorList.clear();
            conductorList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

// este metodo crea la vista del layout y sus componenetes

    // 2.- se define en una clase el  ViewHolder y heredamos de  RecyclerView.ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declaramos en variables de los elelentos que se tienen en el archivo xml que se van a mostrar a futuro faltaria el ID
        CardView cardView;
        ImageView imagencandado;
        TextView textotitulo3placa;
        EditText escribaSuContrena;

        //3.- constructor del viewholder
        public ViewHolder(View itemView){

            super(itemView);
            // castin de los elementos relacionamos las variables del ViewHolder haiendo el castin con los elelemnto por su ID del archivo XML

            cardView = (CardView)itemView.findViewById(R.id.cardviewConductorXML);
            imagencandado = (ImageView)itemView.findViewById(R.id.imagenCandado);
            textotitulo3placa=(TextView)itemView.findViewById(R.id.titulo3PlacaXML);

        }
    }




}







