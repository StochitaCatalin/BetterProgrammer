package com.stochitacatalin.betterprogrammer.ui.main;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.stochitacatalin.betterprogrammer.Database.DatabaseHelper;
import com.stochitacatalin.betterprogrammer.Database.SectionComponentContract;
import com.stochitacatalin.betterprogrammer.Games.MineSweeper.MineSweeperFragment;
import com.stochitacatalin.betterprogrammer.GamesLearnActivity;
import com.stochitacatalin.betterprogrammer.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import es.dmoral.toasty.Toasty;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String TAB_POSITION = "tab_position";

    private SectionsPagerAdapter.TabInfo tabInfo;
    private int position;
    private SectionComponentItem[] sectionComponentItems;
    private ViewPager viewPager;

    public static PlaceholderFragment newInstance(int position) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAB_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewPager = ((GamesLearnActivity) context).viewPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(TAB_POSITION);
            tabInfo = ((SectionsPagerAdapter)(viewPager.getAdapter())).getTabs()[position];
            SQLiteDatabase database = new DatabaseHelper(getContext()).getReadableDatabase();
            sectionComponentItems = retriveSectionComponents(database);
        }
        else
            sectionComponentItems = new SectionComponentItem[0];
    }

    SectionComponentItem[] retriveSectionComponents(SQLiteDatabase db){

        String[] projection = {
                BaseColumns._ID,
                SectionComponentContract.SectionComponentEntry.COLUMN_NAME_TYPE,
                SectionComponentContract.SectionComponentEntry.COLUMN_NAME_DATA,
        };

        String selection = SectionComponentContract.SectionComponentEntry.COLUMN_NAME_SECTION + " = ?";
        System.out.println(tabInfo.id);
        String [] selectionArgs = {String.valueOf(tabInfo.id)};

        String sortOrder = SectionComponentContract.SectionComponentEntry.COLUMN_NAME_ORDER + " ASC";

        Cursor cursor = db.query(
                SectionComponentContract.SectionComponentEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        SectionComponentItem[] components = new SectionComponentItem[cursor.getCount()];
        int i = 0;
        while(cursor.moveToNext()) {
            components[i] = new SectionComponentItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            i++;
        }

        cursor.close();
        return components;
    }
    public class SectionComponentItem {
        int _ID;
        String type;
        String data;
        SectionComponentItem(int _ID,String type,String data){
            this._ID = _ID;
            this.type = type;
            this.data = data;
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view;
        int lastIndex = 0;
        int nextIndex = 0;
        if(tabInfo.type.equals("quiz")) {
            view = inflater.inflate(R.layout.fragment_main_quiz, container, false);
            lastIndex++;
        }
        else
            view = inflater.inflate(R.layout.fragment_main, container, false);
        LinearLayout root = view.findViewById(R.id.root);
        int dp_8 = (int) getResources().getDimension(R.dimen.dp_8);
        for (SectionComponentItem component : sectionComponentItems) {
            if (component.type.equals("text")) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                params.setMargins(dp_8, dp_8, dp_8, dp_8);
                TextView textView = new TextView(getContext());
                textView.setTypeface(Typeface.SERIF);
                //textView.setTypeface(Typeface.MONOSPACE);
                //textView.setTypeface(Typeface.SANS_SERIF);
                textView.setTextSize(getResources().getDimension(R.dimen.text_size_6sp));
                textView.setLayoutParams(params);
                textView.setText(Html.fromHtml(component.data, Html.FROM_HTML_MODE_COMPACT));
                root.addView(textView,nextIndex);
                nextIndex++;
                lastIndex++;
            } else if (component.type.equals("fragment")) {
                Fragment fragment = addFragmentToView(component, root);
                if (fragment != null && tabInfo.type.equals("quiz")) {
                    if (fragment instanceof MineSweeperFragment) {
                        ((MineSweeperFragment) fragment).onGameEnd = new MineSweeperFragment.OnGameEnd() {
                            @Override
                            public void onWon() {
                                tabInfo.completed = true;
                                complete();
                               // unlockNext(false);
                            }

                            @Override
                            public void onLost() {

                            }
                        };
                    }
                }
            } else if (component.type.equals("radio")) {
                RadioGroup radioGroup = root.findViewById(R.id.radioGroup);
                if (radioGroup == null) {
                    radioGroup = new RadioGroup(getContext());
                    radioGroup.setId(R.id.radioGroup);
                    root.addView(radioGroup,nextIndex);
                    nextIndex++;
                    lastIndex++;
                }
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(Html.fromHtml(component.data, Html.FROM_HTML_MODE_COMPACT));
                radioGroup.addView(radioButton,nextIndex);
                nextIndex++;
                lastIndex++;
            } else {
                Toast.makeText(getContext(), "Type " + component.type + " not supported!", Toast.LENGTH_LONG).show();
            }
        }

        //if type == learn (else if tab == quiz)
        MaterialButton materialButton = new MaterialButton(getContext());
        materialButton.setText("Continue");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dp_8, dp_8, dp_8, dp_8);
        materialButton.setLayoutParams(params);
        root.addView(materialButton,lastIndex);
       // nextIndex++;

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(complete()){
                    moveToNext();
                }
            }
        });

        return view;
    }

    public boolean complete(){
        if (tabInfo.type.equals("learn")) {
            ((SectionsPagerAdapter) viewPager.getAdapter()).complete(position);
            return true;
        } else if (tabInfo.type.equals("quiz")) {
            if (tabInfo.completed) {
                ((SectionsPagerAdapter) viewPager.getAdapter()).complete(position);
                return true;
            }
        }else {
            Toasty.warning(getContext(), "Tab type " + tabInfo.type + " unknown!").show();
        }
        return false;
    }


    void moveToNext() {
        if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
            getActivity().onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    public Fragment addFragmentToView(SectionComponentItem component,LinearLayout root){
        try {
           // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            Class<?> fragmentClass = Class.forName(component.data);
            // Class<?> fragmentClass = Class.forName("com.stochitacatalin.betterprogrammer.Games.MineSweeper.MineSweeperFragment");
            //  Constructor<?> constructor = fragmentClass.getConstructor(String.class, Integer.class);
            Constructor<?> constructor = fragmentClass.getConstructor();
            // Fragment instance = (Fragment) constructor.newInstance("stringparam", 42);
            Fragment instance = (Fragment) constructor.newInstance();
            FragmentManager fragmentManager =getChildFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_id,instance);
            ft.commit();
            return instance;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}