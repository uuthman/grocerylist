package com.example.android.grocerylistitem.Ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.grocerylistitem.Activities.DetailActivity;
import com.example.android.grocerylistitem.Data.DatabaseHandler;
import com.example.android.grocerylistitem.Model.Grocery;
import com.example.android.grocerylistitem.R;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceries;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceries) {
        this.context = context;
        this.groceries = groceries;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceries.get(position);

        holder.groceryName.setText(grocery.getName());
        holder.groceryQty.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryName;
        public TextView groceryQty;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            groceryName = (TextView) itemView.findViewById(R.id.grocery_name);
            groceryQty = (TextView) itemView.findViewById(R.id.grocery_qty);
            dateAdded = (TextView) itemView.findViewById(R.id.date_added);
            editButton = (Button) itemView.findViewById(R.id.edit_button);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //go to the other screen
                    int position = getAdapterPosition();

                    Grocery grocery = groceries.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.edit_button:
                    int position = getAdapterPosition();
                    Grocery grocery = groceries.get(position);
                    editItem(grocery);
                    break;
                case R.id.delete_button:
                     position = getAdapterPosition();
                     grocery = groceries.get(position);
                    deleteItem(grocery.getId());
                    break;
            }
        }

        public void deleteItem(final int id){

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noConf);
            Button yesButton = (Button) view.findViewById(R.id.yesConf);

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    //remove from db
                    db.deleteCategory(id);

                    //remove from adapter
                    groceries.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    alertDialog.dismiss();
                }
            });
        }


        public void editItem(final Grocery grocery){

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            final EditText item = (EditText) view.findViewById(R.id.enter_grocery);
            final EditText qty = (EditText) view.findViewById(R.id.qty);
            final TextView header = (TextView) view.findViewById(R.id.titleHeader);
            header.setText("Update grocery item");


            Button saveButton = (Button) view.findViewById(R.id.add_item);

            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    header.setText("Update grocery item");
                    grocery.setName(item.getText().toString());
                    grocery.setQuantity(qty.getText().toString());

                    if (!item.getText().toString().isEmpty() && !qty.getText().toString().isEmpty()){
                        db.updateCategory(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                        alertDialog.dismiss();
                    }else {
                        Snackbar.make(view, "Add grocery and quantity", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
