package com.cs619.alpha.bulletzone.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.cs619.alpha.bulletzone.R;
import com.cs619.alpha.bulletzone.model.Tank;
import com.cs619.alpha.bulletzone.model.TankClientActivity;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

/**
 * Populates UI based on gamestate.
 */
@EBean
public class GridAdapter extends BaseAdapter {
  public static final String TAG = GridAdapter.class.getSimpleName();

  private final Object monitor = new Object();
  private Context context;

  private int lastTankX = -1;
  private int lastTankY = -1;

  @SystemService
  protected LayoutInflater inflater;
  private int[][] mEntities = new int[16][16];
  private Tank tank;

  /**
   * Passes the grid from pollertask
   *
   * @param entities
   */
  public void updateList(int[][] entities) {
    synchronized (monitor) {
      this.mEntities = entities;
      this.notifyDataSetChanged();

      checkPulse();
      ((TankClientActivity) context).updateHP(tank.getHealth());
    }
  }

  /**
   * Passes the context to the class
   *
   * @param context
   */
  public void setContext(Context context) {
    this.context = context;
  }

  /**
   * set tank id.
   *
   * @param tank Tank
   */
  public void setTank(Tank tank) {
    this.tank = tank;
  }

  /**
   * get number of slots on board. Used for adapter pattern.
   *
   * @return int
   */
  @Override
  public int getCount() {
    return 16 * 16;
  }

  /**
   * get item based on position. Used for adapter pattern.
   *
   * @param position int
   * @return Object
   */
  @Override
  public Object getItem(int position) {
    return mEntities[position / 16][position % 16];
  }

  /**
   * get itemid based on position.
   *
   * @param position int
   * @return long
   */
  @Override
  public long getItemId(int position) {
    return position;
  }

  /**
   * Get specific view based on position and context.
   *
   * @param position int
   * @param view     View
   * @param parent   ViewGroup
   * @return View
   */
  @Override
  public View getView(int position, View view, ViewGroup parent) {

    if (view == null) {
      view = inflater.inflate(R.layout.field_item, null);
      int iconDimen = parent.getContext().getResources().getDisplayMetrics().widthPixels / 16;
      view.setLayoutParams(new GridView.LayoutParams(iconDimen, iconDimen));
      ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);

    }

    int row = position / 16;
    int col = position % 16;

    int val = mEntities[row][col];

//    checkPulse();

//    If the value is 1TIDLIFX, then the ID of the tank is TID, it has LIF life and its direction is X.
//    (E.g., value = 12220071, tankId = 222, life = 007, direction = 2). Directions: {0 - UP, 2 - RIGHT, 4 - DOWN, 6 - LEFT}
    synchronized (monitor) {
      if (val > 0) {
        if (isWall(val)) {
          ((ImageView) view).setImageResource(R.drawable.wall);

        } else if (isDestructableWall(val)) {
          ((ImageView) view).setImageResource(R.drawable.destructable_wall);

        } else if (isBullet(val)) {
          int dmg = ((val % 1000) - (val % 10)) / 10;
          if (dmg == 10) {
            ((ImageView) view).setImageResource(R.drawable.bullet1);
          } else if (dmg == 30) {
//              ((ImageView) view).setImageResource(R.drawable.bullet2);
          } else {
//              ((ImageView) view).setImageResource(R.drawable.bullet3);
          }

        } else if (isTank(val)) {
          int direction, up, right, down, left, life;

          direction = val % 10;

          life = ((val % 10000) - (val % 10)) / 10;

          if (tank.getId() == decodeTankId(val)) {
            tank.setHealth(life);

            lastTankX = col;
            lastTankY = row;
            up = R.drawable.tank_up_blue;
            right = R.drawable.tank_right_blue;
            down = R.drawable.tank_down_blue;
            left = R.drawable.tank_left_blue;
          } else {
            up = R.drawable.tank_up;
            right = R.drawable.tank_right;
            down = R.drawable.tank_down;
            left = R.drawable.tank_left;
          }

          if (direction == 0) {
            ((ImageView) view).setImageResource(up);
          } else if (direction == 2) {
            ((ImageView) view).setImageResource(right);
          } else if (direction == 4) {
            ((ImageView) view).setImageResource(down);
          } else if (direction == 6) {
            ((ImageView) view).setImageResource(left);
          }
        }
      } else {
        ((ImageView) view).setImageDrawable(null);
      }

    }
//    }

    return view;
  }

  private boolean isWall(int val) {
    return val == 1000;
  }

  private boolean isDestructableWall(int val) {
    return val == 1500;
  }

  private boolean isBullet(int val) {
    return val >= 2000000 && val <= 3000000;
  }

  private boolean isTank(int val) {
    return val >= 10000000 && val <= 20000000;
  }

  private int decodeTankId(int val) {
    if (val < 10000000 || val > 20000000) {
      return -1;
    } else {
      return (val / 10000) - (val / 10000000) * 1000;
    }
  }

  /**
   * game deletes tank b4 life is reported at zero. need a way to check if we're still kickin
   */
  private void checkPulse() {
    boolean christLives = false;
    if (tank.getHealth() != 0 && lastTankX != -1 && lastTankY != -1) {
      if (tank.getId() == decodeTankId(mEntities[(lastTankX - 1) % 16][lastTankY]) ||
          tank.getId() == decodeTankId(mEntities[(lastTankX + 1) % 16][lastTankY]) ||
          tank.getId() == decodeTankId(mEntities[lastTankX][lastTankY]) ||
          tank.getId() == decodeTankId(mEntities[lastTankX][(lastTankY + 1) % 16]) ||
          tank.getId() == decodeTankId(mEntities[lastTankX][(lastTankY + 1) % 16])) {
        christLives = true;
      }

      if (!christLives) {
        tank.setHealth(0);
        ((TankClientActivity) context).updateHP(0);
      }
    }
  }
}


