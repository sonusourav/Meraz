package com.sonusourav.merazoverflow.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.sonusourav.merazoverflow.R;
import java.io.IOException;
import java.util.Objects;

public class UsersFragment extends Fragment {

  private SurfaceView mCameraView;
  private TextView mTextView;
  private CameraSource mCameraSource;
  private SearchView searchView;

  public UsersFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_users, container, false);
    mCameraView = view.findViewById(R.id.surface_camera_preview);
    mTextView = view.findViewById(R.id.text_view);
    searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.search_view);

    startCameraSource();
    return view;
  }

  private void startCameraSource() {

    //Create the TextRecognizer
    final TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();

    if (!textRecognizer.isOperational()) {
      Log.w("Questions", "Detector dependencies not loaded yet");
    } else {

      mCameraSource = new CameraSource.Builder(getActivity(), textRecognizer)
          .setFacing(CameraSource.CAMERA_FACING_BACK)
          .setRequestedPreviewSize(720, 480)
          .setAutoFocusEnabled(true)
          .setRequestedFps(2.0f)
          .build();

      mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
          try {
            mCameraSource.start(mCameraView.getHolder());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
          mCameraSource.stop();
        }
      });

      //Set the TextRecognizer's Processor.
      textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
        @Override
        public void release() {
        }

        @Override
        public void receiveDetections(Detector.Detections<TextBlock> detections) {
          final SparseArray<TextBlock> items = detections.getDetectedItems();
          if (items.size() != 0) {

            mTextView.post(new Runnable() {
              @Override
              public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder query = new StringBuilder();

                for (int i = 0; i < items.size(); i++) {
                  TextBlock item = items.valueAt(i);
                  stringBuilder.append(item.getValue());
                  query.append(item.getValue());
                  query.append(" ");
                  stringBuilder.append("\n");
                }
                mTextView.setText(stringBuilder.toString());
                searchView.setQuery(query.toString(), false);
              }
            });
          }
        }
      });
    }
  }
}
