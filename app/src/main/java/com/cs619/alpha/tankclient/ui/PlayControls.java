package com.cs619.alpha.tankclient.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs619.alpha.tankclient.R;
import com.cs619.alpha.tankclient.Tank;
import com.cs619.alpha.tankclient.rest.BulletZoneRestClient;
import com.cs619.alpha.tankclient.util.BooleanWrapper;

/**
 * Created by Chris Oelerich on 4/27/16.
 */
public class PlayControls extends Fragment implements View.OnClickListener {
  private static final String TAG = PlayControls.class.getSimpleName();
  private BulletZoneRestClient restClient;
  private Tank t;
  private BooleanWrapper bw;

  public static PlayControls newInstance(BulletZoneRestClient bulletZoneRestClient, Tank tank) {

    PlayControls fragment = new PlayControls();
    fragment.restClient = bulletZoneRestClient;
    fragment.t = tank;

    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.play_control_view, container, false);

    (view.findViewById(R.id.buttonFire1)).setOnClickListener(this);
    (view.findViewById(R.id.buttonFire2)).setOnClickListener(this);
    (view.findViewById(R.id.buttonFire3)).setOnClickListener(this);
    (view.findViewById(R.id.buttonLeft)).setOnClickListener(this);
    (view.findViewById(R.id.buttonRight)).setOnClickListener(this);
    (view.findViewById(R.id.buttonUp)).setOnClickListener(this);
    (view.findViewById(R.id.buttonDown)).setOnClickListener(this);

    // Inflate the layout for this fragment
    return view;
  }

  @Override
  public void onClick(View v) {

    Log.d(TAG, "onClick() called with: " + "v = [" + v + "]");

    switch (v.getId()) {
      case R.id.buttonFire1:
        fire(1);
        break;
      case R.id.buttonFire2:
        fire(2);
        break;
      case R.id.buttonFire3:
        fire(3);
        break;
      case R.id.buttonLeft:
        left();
        break;
      case R.id.buttonRight:
        right();
        break;
      case R.id.buttonUp:
        up();
        break;
      case R.id.buttonDown:
        down();
        break;
    }
  }

  /**
   * fire all phasers.
   *
   * @param i long
   */
  public void fire(int i) {
    if (t.getId() != -1) {
      try {
        restClient.fire(t.getId(), i);
      } catch (Exception e) {
        Log.e(TAG, "fire: ", e);
      }
    }
  }

  /**
   * move t.
   */
  public void down() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 4)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 0) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
          bw = restClient.turn(t.getId(), (byte) 4);
          if (bw.isResult()) {
            t.setDir(4);
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "move: ", e);
      }
    }
  }

  public void up() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 0)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 4) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
          bw = restClient.turn(t.getId(), (byte) 0);
          if (bw.isResult()) {
            t.setDir(0);
          }
        }
        //restClient.move(t.getId(), (byte) t.getDir());
      } catch (Exception e) {
        Log.e(TAG, "move: ", e);
      }
    }
  }

  /**
   * turn t.
   */
  public void left() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 6)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 2) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
          bw = restClient.turn(t.getId(), (byte) 6);
          if (bw.isResult()) {
            t.setDir(6);
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "turn: ", e);
      }
    }
  }

  public void right() {
    if (t.getId() != -1) {
      try {
        if (t.getDir() == 2)
          restClient.move(t.getId(), (byte) t.getDir());
        else if (t.getDir() == 6) {
          restClient.move(t.getId(), (byte) t.getRevDir());
        } else {
          bw = restClient.turn(t.getId(), (byte) 2);
          if (bw.isResult()) {
            t.setDir(2);
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "turn: ", e);
      }
    }
  }


}
