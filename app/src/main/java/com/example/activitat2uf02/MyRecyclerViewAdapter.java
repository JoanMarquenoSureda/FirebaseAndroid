package com.example.activitat2uf02;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{


    private List<Vehicle> elements;
    private RecyclerViewClickListener clickListener;

    private Context context;
    private DatabaseReference databaseReference;


    public MyRecyclerViewAdapter(Context context, DatabaseReference databaseReference, List<Vehicle> elements, RecyclerViewClickListener listener) {
        this.context = context;
        this.databaseReference = databaseReference;
        this.elements = elements;
        this.clickListener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View viewElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        return new ViewHolder(viewElement);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        holder.getTextNom().setText(elements.get(position).getNom()+" ");
        holder.getTextCognom().setText(elements.get(position).getCognoms());
        holder.getTextTelefon().setText(elements.get(position).getTelefon());
        holder.getTextMatricula().setText(elements.get(position).getMatricula());
        holder.getTextMarca().setText(elements.get(position).getMarcaVehicle());
        holder.getTextModel().setText(elements.get(position).getModelVehicle());

    }

    @Override
    public int getItemCount() {
        return elements.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNom;
        private TextView txtCognom;
        private TextView txtTelefon;
        private TextView txtMatricula;
        private TextView txtMarca;
        private TextView txtModel;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNom = itemView.findViewById(R.id.textNom);
            txtCognom = itemView.findViewById(R.id.textCognom);
            txtTelefon = itemView.findViewById(R.id.textTelefon);
            txtMatricula = itemView.findViewById(R.id.textMatricula);
            txtMarca = itemView.findViewById(R.id.textMarca);
            txtModel = itemView.findViewById(R.id.textModel);

            itemView.setOnClickListener(this);
        }

        private void ensenarElement(View v, int position) {
            PopupMenu popupMenu = new PopupMenu(context, v);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.menu_contextual, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new Menu(position));
            popupMenu.show();


        }

        public class Menu implements PopupMenu.OnMenuItemClickListener {
            Integer pos;
            public Menu(Integer pos) {
                this.pos = pos;
            }
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.Modificar:

                        Vehicle vehicleModificar = elements.get(pos);
                        System.out.println(vehicleModificar);
                        clickListener.onItemClick(vehicleModificar);

                        return true;

                    case R.id.Eliminar:
AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Estas segur que vols eliminar aquest element?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        databaseReference.child(elements.get(pos).getMatricula()).removeValue();
                                        Toast.makeText(context, "Element eliminat", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(context, "Element no eliminat", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder.create();
                        builder.show();
                        return true;

                    case R.id.Cancelar:

                        return true;
                    default:
                        return false;
                }
            }
        }

        public TextView getTextNom() {
            return txtNom;
        }
        public TextView getTextCognom() {
            return txtCognom;
        }
        public TextView getTextTelefon() {
            return txtTelefon;
        }
        public TextView getTextMatricula() {
            return txtMatricula;
        }
        public TextView getTextMarca() {
            return txtMarca;
        }
        public TextView getTextModel() {
            return txtModel;
        }

        @Override
        public void onClick(View v) {
            ensenarElement(v, getAdapterPosition());

        }


    }
}
