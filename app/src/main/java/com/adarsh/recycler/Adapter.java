package com.adarsh.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
import static java.lang.Integer.parseInt;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<dataitem> items;
    RecyclerView mRecyclerView;
    private Context context;
    private TextView t;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText in, out;
        public TextView itemno, totalefforts;

        public MyViewHolder(final View view) {
            super(view);
            in = (EditText) view.findViewById(R.id.intime);
            out = (EditText) view.findViewById(R.id.outtime);
            totalefforts = (TextView) view.findViewById(R.id.totaleffort);
            itemno = (TextView) view.findViewById(R.id.itemname);

            in.setInputType(TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_NUMBER);
            in.addTextChangedListener(new TextWatcher() {
                String beforeTXT;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    Log.i("WWWWWWWWWWWWW", " BEFORE : " + s + " and " + start + " and " + count + "and " + after + " BEFORETXT " + beforeTXT);
                    beforeTXT = "" + s;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int input;


                    Log.i("0000", "/n" + start + "char" + s);
                    boolean alpha = false;
                    if (s.toString().length() >= 1) {
                        String[] array = s.toString().split("");
                        for (int i = 0; i < array.length; i++) {
                            if (array[i].matches("[a-zA-Z]")) {
                                Log.d("regex", "matches :" + array[i] + " in " + s.toString());
                                alpha = true;
                            }


                        }

                    }

                    if (!alpha) {
                        if (!s.toString().matches("[a-zA-Z]")) {
                            //first determine whether user is at hrs side or min side
                            if (s.toString().equals("")) {
                                return;
                            }
                            if (s.toString().length() > 2 && start <= 2) { //means the user is at hour side
                                input = parseInt(s.toString().substring(0, 1)) % 10;

                            } else if (s.toString().length() > 2 && start >= 3) {//means that user is at min side
                                input = parseInt("0" + s.toString().substring(3)) % 10;

                            } else if (s.toString().indexOf(":") == 1) { // if we have for eg 1: or 0: then we take first character for parsing
                                input = parseInt(s.toString().charAt(0) + "");
                            } else { //else it is default where the user is at first position
                                input = parseInt(s.toString()) % 10;
                            }

                            //Special case where 00: is autommatically converted to 12: in 12hr time format
                            if (s.toString().contains("00:")) {
                                Log.i("INsisde )))", "i am called ");
                                in.setText("12:");
                                return;
                            }

                            if (beforeTXT.contains(":") && s.toString().length() == 2) {

                                in.setText("");
                            }


                            //Now we manipulate the input and its formattin and cursor movement
                            if (input <= 1 && start == 0) { //thiis is for first input value to check .... time shouldnt exceed 12 hr
                                //do nothing
                            } else if (input > 2 && start == 0 && !s.toString().startsWith("1")) { //if at hour >1 is press then automaticc set the time as 02: or 05: etc
                                in.setText("0" + s + ":");
                            } else if (input > 3 && start == 1 && !s.toString().startsWith("0") && s.toString().startsWith("2")) { //whe dont have greater than 12 hrs so second postionn shouldn't exceed value 2
                                in.setText(beforeTXT);
                            } else if (start == 1 && !s.toString().startsWith("0") && s.toString().startsWith("1") && !s.toString().contains(":") && beforeTXT.length() < s.toString().length()) { //whe dont have greater than 12 hrs so second postionn shouldn't exceed value 2
                                in.setText(s.toString() + ":");
                            } else if (start == 1 && s.toString().startsWith("1")) { //whe dont have greater than 12 hrs so second postionn shouldn't exceed value 2
                                in.setText(s.toString());
                            } else if (start == 1 && !beforeTXT.contains(":")) {  //if valid input 10 or 11 or 12 is given then convert it to 10: 11: or 12:
                                in.setText(s.toString() + ":");

                                if (s.toString().length() == 1 && s.toString().startsWith("2")) {
                                    in.setText("");
                                }
                                if (s.toString().length() == 2 && s.toString().startsWith("1")) {
                                    in.setText("");
                                }
                                if (s.toString().startsWith("1") && s.toString().length() == 1 && s.toString().contains(":")) { //on back space convert 1: to 01:
                                    in.setText("");
                                }

                            } else if (start == 3 && input > 5) { //min fig shouldn't exceed 59 so ...if at first digit of min input >5 then do nothing or codpy the earlier text
                                in.setText(beforeTXT);
                            } else if (start > 4 && s.toString().length() > 5) { // the total string lenght shouldn't excced 5
                                in.setText(beforeTXT);
                            } else if (start < 2 && beforeTXT.length() > 2 && alpha) {
                                in.setText(beforeTXT);

                            }

                        }
                    } else {
                        in.setText(beforeTXT);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.i("after  TEXT TEXXT", " this : " + s);
                    in.setSelection(in.getText().toString().length());


                    if (in.length() == 5) {

                        out.requestFocus();
                    }
                }
            });


            out.setInputType(TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_NUMBER);


        }

    }


    public Adapter(List<dataitem> item, Context c, TextView total) {
        this.items = item;
        this.context = c;
        this.t = total;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final dataitem obj = items.get(position);
        holder.itemno.setText((Integer.toString(obj.getNumber())));
        holder.totalefforts.setHint("HH:MM");
        holder.totalefforts.setText(obj.getTotalEfforts());

        holder.out.addTextChangedListener(new TextWatcher() {
            String beforeTXT;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.i("WWWWWWWWWWWWW", " BEFORE : " + s + " and " + start + " and " + count + "and " + after + " BEFORETXT " + beforeTXT);
                beforeTXT = "" + s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int input;


                Log.i("0000", "/n" + start + "char" + s);
                boolean alpha = false;
                if (s.toString().length() >= 1) {
                    String[] array = s.toString().split("");
                    for (int i = 0; i < array.length; i++) {
                        if (array[i].matches("[a-zA-Z]")) {
                            Log.d("regex", "matches :" + array[i] + " in " + s.toString());
                            alpha = true;
                        }


                    }

                }

                if (!alpha) {
                    if (!s.toString().matches("[a-zA-Z]")) {
                        //first determine whether user is at hrs side or min side
                        if (s.toString().equals("")) {
                            return;
                        }
                        if (s.toString().length() > 2 && start <= 2) { //means the user is at hour side
                            input = parseInt(s.toString().substring(0, 1)) % 10;

                        } else if (s.toString().length() > 2 && start >= 3) {//means that user is at min side
                            input = parseInt("0" + s.toString().substring(3)) % 10;

                        } else if (s.toString().indexOf(":") == 1) { // if we have for eg 1: or 0: then we take first character for parsing
                            input = parseInt(s.toString().charAt(0) + "");
                        } else { //else it is default where the user is at first position
                            input = parseInt(s.toString()) % 10;
                        }

                        //Special case where 00: is autommatically converted to 12: in 12hr time format
                        if (s.toString().contains("00:")) {
                            Log.i("INsisde )))", "i am called ");
                            holder.out.setText("12:");
                            return;
                        }

                        if (beforeTXT.contains(":") && s.toString().length() == 2) {

                            holder.out.setText("");
                        }


                        //Now we manipulate the input and its formattin and cursor movement
                        if (input <= 1 && start == 0) { //thiis is for first input value to check .... time shouldnt exceed 12 hr
                            //do nothing
                        } else if (input > 2 && start == 0 && !s.toString().startsWith("1")) { //if at hour >1 is press then automaticc set the time as 02: or 05: etc
                            holder.out.setText("0" + s + ":");
                        } else if (input > 3 && start == 1 && !s.toString().startsWith("0") && s.toString().startsWith("2")) { //whe dont have greater than 12 hrs so second postionn shouldn't exceed value 2
                            holder.out.setText(beforeTXT);
                        } else if (start == 1 && !s.toString().startsWith("0") && s.toString().startsWith("1") && !s.toString().contains(":") && beforeTXT.length() < s.toString().length()) { //whe dont have greater than 12 hrs so second postionn shouldn't exceed value 2
                            holder.out.setText(s.toString() + ":");
                        } else if (start == 1 && s.toString().startsWith("1")) { //whe dont have greater than 12 hrs so second postionn shouldn't exceed value 2
                            holder.out.setText(s.toString());
                        } else if (start == 1 && !beforeTXT.contains(":")) {  //if valid input 10 or 11 or 12 is given then convert it to 10: 11: or 12:
                            holder.out.setText(s.toString() + ":");

                            if (s.toString().length() == 1 && s.toString().startsWith("2")) {
                                holder.out.setText("");
                            }
                            if (s.toString().length() == 2 && s.toString().startsWith("1")) {
                                holder.out.setText("");
                            }
                            if (s.toString().startsWith("1") && s.toString().length() == 1 && s.toString().contains(":")) { //on back space convert 1: to 01:
                                holder.out.setText("");
                            }

                        } else if (start == 3 && input > 5) { //min fig shouldn't exceed 59 so ...if at first digit of min input >5 then do nothing or codpy the earlier text
                            holder.out.setText(beforeTXT);
                        } else if (start > 4 && s.toString().length() > 5) { // the total string lenght shouldn't excced 5
                            holder.out.setText(beforeTXT);
                        } else if (start < 2 && beforeTXT.length() > 2 && alpha) {
                            holder.out.setText(beforeTXT);

                        }

                    }
                } else {
                    holder.out.setText(beforeTXT);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("after  TEXT TEXXT", " this : " + s);
                holder.out.setSelection(holder.out.getText().toString().length());

                if (beforeTXT.length() > s.length()) {
                    holder.totalefforts.setText("HH:MM");

                }


                if (holder.out.length() == 5) {


                    Integer hourin = null;
                    Integer minin = null;
                    Integer hourout = null;
                    Integer minout = null;
                    String[] timein = holder.in.getText().toString().split(":");
                    String[] timeout = holder.out.getText().toString().split(":");

                    hourin = parseInt(timein[0]);
                    minin = parseInt(timein[1]);

                    hourout = parseInt(timeout[0]);
                    minout = parseInt(timeout[1]);

                    int diffmin;
                    int diffhour;
                    Log.v("Data1", " " + hourin + " :" + minin + " -----" + hourout + " :" + minout);

                    if (hourin == hourout) {
                        diffmin = minin - minout;
                        if (diffmin <= -1) {
                            int d = (-1) * diffmin;
                            if (d < 10) {
                                holder.totalefforts.setText("00" + ":" + "0" + d);
                            } else {
                                holder.totalefforts.setText("00" + ":" + d);
                            }
                        } else {

                            if (diffmin == 0) {
                                holder.out.setError("OUT can not be equal to IN");
                            }
                            holder.out.setError("OUT can not be less than IN");
                        }

                    } else {
                        int min;
                        diffhour = hourin - hourout;
                        diffmin = minin - minout;
                        if (minin > minout) {
                            min = diffmin;
                        } else {
                            min = (-1) * diffmin;

                        }
                        if (diffhour <= -1) {
                            int hr = (-1) * diffhour;


                            if (hr < 10 && min < 10) {
                                holder.totalefforts.setText("0" + hr + ":" + "0" + min);
                            } else if (hr > 10 && min > 10) {
                                holder.totalefforts.setText(hr + ":" + min);

                            } else if (hr < 10 && min > 10) {
                                holder.totalefforts.setText("0" + hr + ":" + min);
                            } else if (hr > 10 && min < 10) {
                                holder.totalefforts.setText(hr + ":" + "0" + min);
                            }

                        } else {
                            if (diffhour == 0) {
                                holder.out.setError("OUT can not be equal to IN");
                            }
                            holder.out.setError("OUT can not be less than IN");

                        }

                    }

//                        SimpleDateFormat format = new SimpleDateFormat("KK:mm",Locale.ENGLISH);
//                        Date date1 = null;
//                        Date date2=null;
//                        try {
//                            date1 = format.parse(in.getText().toString());
//                            date2 = format.parse(out.getText().toString());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        long mills = date1.getTime() - date2.getTime();
//                        Log.v("Data1", ""+date1.getTime());
//                        Log.v("Data1", ""+date1.getTime()/(1000 * 60 * 60) +"MIn "+(date1.getTime()/(1000*60)) % 60);
//                        Log.v("Data1", ""+date2.getTime()/(1000 * 60 * 60) +"MIn "+(date2.getTime()/(1000*60)) % 60);
//                        int hours = (int) (mills/(1000 * 60 * 60));
//                        int mins = (int) (mills/(1000*60)) % 60;
//
//                        String diff = hours + ":" + mins;
//                        Log.v("Data1", diff);


                }
//                    else
//                    {
//
//                        total.setText("");
//                    }

            }
        });


        holder.totalefforts.addTextChangedListener(new TextWatcher()

        {
            Integer beforeTotal = 0;
            Integer newTotal = 0;
            Integer hour = 0;
            Integer bfhour = 0;
String beTotal=null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
beTotal =""+s;
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                  // ViewParent parent=view.getParent();
//

                if (!holder.totalefforts.getText().toString().contains("HH:MM") || !s.toString().contains("0")) {

                    if (!holder.totalefforts.getText().toString().contains("HH:MM")) {

                        // t.setText("total will com here");

                        for (int i = 0; i < items.size(); i++) {


                            if (i == position)
                            {
//
                                final String time = holder.totalefforts.getText().toString();

                                String timeArray[] = time.split(":");
                                hour = Integer.parseInt(timeArray[0]);
                                Integer min = Integer.parseInt(timeArray[1]);

                                if (!t.getText().toString().isEmpty()) {
                                    beforeTotal = Integer.parseInt((String) t.getText());

                                    Log.d("INput", "" + "Current hour : " + hour + "  before total" + beforeTotal);
                                }
                                // totalPrice = hour+beforeTotal;
//                        if(items.get(i).getTotalEfforts()!=0)
//
//                            totalPrice += efforts;
                                newTotal = hour + beforeTotal;
                                t.setText(Integer.toString(hour + beforeTotal));


//                        //}
                            }
                        }

                    } else

                    {


//                        newTotal = Integer.parseInt((String) t.getText());
                        if (holder.out.getText().length() == 4)
                        {
                            for (int i = 0; i < items.size(); i++)

                            {
                                Integer time=0;
                                if(!t.getText().toString().isEmpty())
                                {
                                     time = Integer.parseInt((String) t.getText());
                                }
//                                final String time = beTotal.toString();

//
//                                String timeArray[] = time.split(":");
//                                hour = Integer.parseInt(timeArray[0]);

                                if (i == position)
                                {

                                    if (time > hour) {
                                        t.setText(Integer.toString(time - hour));
                                    } else if (time == hour) {
                                        t.setText(Integer.toString(0));
                                    } else if (time < hour) {
                                        t.setText(Integer.toString(hour - time));
                                    }
//
                                }
                            }

                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


                // t.setText(totalPrice);


            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }
}
