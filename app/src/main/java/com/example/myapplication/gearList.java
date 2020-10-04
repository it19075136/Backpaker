package com.example.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class gearList extends ArrayAdapter<campingGear> {

    private Activity context;
    private List<campingGear> campingGearList;

    public gearList(Activity context, List<campingGear> campingGearList){
        super(context,R.layout.list_layout,campingGearList);
        this.context = context;
        this.campingGearList = campingGearList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView txtlocation = (TextView) listViewItem.findViewById(R.id.textViewlocation);
        TextView txtemail = (TextView) listViewItem.findViewById(R.id.textViewmail);
        TextView txtlamps = (TextView) listViewItem.findViewById(R.id.textViewlamps);
        TextView txtcups = (TextView) listViewItem.findViewById(R.id.textViewcupsandplates);
        TextView txthammer = (TextView) listViewItem.findViewById(R.id.textViewhammer);
        TextView txttables = (TextView) listViewItem.findViewById(R.id.textViewtables);
        TextView txtgas = (TextView) listViewItem.findViewById(R.id.textViewgas);
        TextView txtdate = (TextView) listViewItem.findViewById(R.id.textViewdate);
        TextView txttentsize = (TextView) listViewItem.findViewById(R.id.textViewtentsize);
        TextView txttentnumber = (TextView) listViewItem.findViewById(R.id.textViewtentnumber);

        campingGear cgear = campingGearList.get(position);

        txtlocation.setText(cgear.getLocationName());
        txtemail.setText(cgear.getEmail());
        txtlamps.setText(cgear.getLamps());
        txtcups.setText(cgear.getCupsPlates());
        txthammer.setText(cgear.getMalletHammer());
        txttables.setText(cgear.getTables());
        txtgas.setText(cgear.getGas());
        txtdate.setText(cgear.getDate());
        txttentsize.setText(cgear.getTentSize());
        txttentnumber.setText(cgear.getTentNumber());

        return listViewItem;
    }
}
