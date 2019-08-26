package com.hsproject.actlogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActsettingFragment_detail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActsettingFragment_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActsettingFragment_detail extends Fragment implements MapView.MapViewEventListener {

    private static final String TAG = "ActsettingFragment_detail";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SeekBar skbRange;
    private MapView mMapView;
    MapPOIItem marker;
    private Spinner spnActList;
    private Button btnColor;

    public ActsettingFragment_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActsettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActsettingFragment_detail newInstance(String param1, String param2) {
        ActsettingFragment_detail fragment = new ActsettingFragment_detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actsetting_detail, container, false);
        view.setFocusableInTouchMode(true);
        view.requestFocus();


        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Log.d(TAG,"BACK키 감지");
                    ((MainActivity)getActivity()).replaceFragmentDetail(false);
                    return true;
                }
                return false;
            }
        });
        spnActList = view.findViewById(R.id.spnActList);
        final ArrayList<String> actList = new ArrayList<String>();
        actList.add("활동을 선택하세요");

        actList.add("...새로운 항목 추가");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,actList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnActList.setAdapter(adapter);

        spnActList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==spnActList.getCount()-1){
                    Log.d(TAG,"새로운 활동 항목 추가");
                    final EditText edittext = new EditText(getContext());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("활동 추가");
                    builder.setMessage("활동명을 입력하세요");
                    builder.setView(edittext);
                    builder.setPositiveButton("입력",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG,"새로운 활동 항목 '" + edittext.getText() +"' 추가됨");
                                    actList.remove(spnActList.getCount()-1);
                                    actList.add(edittext.getText().toString());
                                    actList.add("...새로운 항목 추가");
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,actList);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spnActList.setAdapter(adapter);
                                    spnActList.setSelection(spnActList.getCount()-2); // 새로 추가된 항목으로 선택
                                }
                            });
                    builder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMapView = (MapView) view.findViewById(R.id.daumMapView);
        mMapView.setMapViewEventListener(this);

        skbRange = (SeekBar) view.findViewById(R.id.skbRange);
        skbRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mMapView.removeAllCircles();

                MapCircle circle1 = new MapCircle(
                        mMapView.getMapCenterPoint(), // center
                        skbRange.getProgress(), // radius
                        Color.argb(128, 255, 0, 0), // strokeColor
                        Color.argb(128, 0, 255, 0) // fillColor
                );
                circle1.setTag(1234);
                mMapView.addCircle(circle1);
            }
        });


        btnColor = view.findViewById(R.id.btnColor);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Show Color Picker: " + btnColor.getText());
                ((MainActivity)getContext()).pickedAct = "==NEWADDEDACT==";
                ColorPickerDialog.newBuilder().show((Activity)getContext());
            }
        });

        // 취소버튼
        ((Button)view.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentDetail(false);
            }
        });

        // 저장버튼
        ((Button)view.findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = 0.0;
                double longitude = 0.0;

                //MapPoint mp = marker.getMapPoint();
                MapPoint.GeoCoordinate mapPointGeo = mMapView.getMapCenterPoint().getMapPointGeoCoord();
                Log.d(TAG,""+mapPointGeo.latitude);

                long result = ((MainActivity)getActivity()).db.insertActSetting(spnActList.getSelectedItem().toString(), ((ColorDrawable) btnColor.getBackground()).getColor(), mapPointGeo.latitude, mapPointGeo.longitude,
                        skbRange.getProgress(), "");

                ((MainActivity)getActivity()).replaceFragmentDetail(false);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onMapViewInitialized(MapView mapView) {
        // MapView had loaded. Now, MapView APIs could be called safely.
        Log.i(TAG, "onMapViewInitialized");

        mapView.removeAllPOIItems();
        mapView.removeAllCircles();

        ContentValues cv = ((MainActivity)getActivity()).db.getLastLocationAsCv();
        if(cv==null) return;

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(cv.getAsDouble(DatabaseHelper.COLUMN_LOCATION_LATITUDE), cv.getAsDouble(DatabaseHelper.COLUMN_LOCATION_LONGITUDE)), true);

        marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(cv.getAsDouble(DatabaseHelper.COLUMN_LOCATION_LATITUDE), cv.getAsDouble(DatabaseHelper.COLUMN_LOCATION_LONGITUDE)));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);


        MapCircle circle1 = new MapCircle(
                MapPoint.mapPointWithGeoCoord(cv.getAsDouble(DatabaseHelper.COLUMN_LOCATION_LATITUDE), cv.getAsDouble(DatabaseHelper.COLUMN_LOCATION_LONGITUDE)), // center
                skbRange.getProgress(), // radius
                Color.argb(128, 255, 0, 0), // strokeColor
                Color.argb(128, 0, 255, 0) // fillColor
        );
        circle1.setTag(1234);
        mapView.addCircle(circle1);

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
        Log.i(TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));

        mapView.removeAllPOIItems();
        mapView.removeAllCircles();

        marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(mapView.getMapCenterPoint());
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        MapCircle circle1 = new MapCircle(
                mapView.getMapCenterPoint(), // center
                skbRange.getProgress(), // radius
                Color.argb(128, 255, 0, 0), // strokeColor
                Color.argb(128, 0, 255, 0) // fillColor
        );
        circle1.setTag(1234);
        mapView.addCircle(circle1);

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        MapPoint.GeoCoordinate mapPointGeo = mapView.getMapCenterPoint().getMapPointGeoCoord();
        Log.i(TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        MapPoint.PlainCoordinate mapPointScreenLocation = mapPoint.getMapPointScreenLocation();
        Log.i(TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        MapPoint.PlainCoordinate mapPointScreenLocation = mapPoint.getMapPointScreenLocation();
        Log.i(TAG, String.format(String.format("MapView onMapViewDoubleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude)));
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        MapPoint.PlainCoordinate mapPointScreenLocation = mapPoint.getMapPointScreenLocation();
        Log.i(TAG, String.format(String.format("MapView onMapViewLongPressed (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude)));
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        MapPoint.PlainCoordinate mapPointScreenLocation = mapPoint.getMapPointScreenLocation();
        Log.i(TAG, String.format("MapView onMapViewDragStarted (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        MapPoint.PlainCoordinate mapPointScreenLocation = mapPoint.getMapPointScreenLocation();
        Log.i(TAG, String.format("MapView onMapViewDragEnded (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(TAG, String.format("MapView onMapViewMoveFinished (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }
}
