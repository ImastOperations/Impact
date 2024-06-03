package in.imast.impact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import in.imast.impact.R;


public class ViewPagerLoginAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {0, R.drawable.mobile_login_1, R.drawable.earn1, R.drawable.rewards};
    private String[] title = {"", "Register", "Earn", "Get Rewarded"};
    private String[] desc = {"", "Register and login to \nyour happiness.", "Redeem the point of happiness \nearn your happiness", "Get the gifts of joy.\nRewards are waiting for you."};

    private boolean isBottom;

    public ViewPagerLoginAdapter(Context context, boolean isBottom) {
        this.context = context;
        this.isBottom = isBottom;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public ImageView img1, img;

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_login, null);
        img1 = (ImageView) view.findViewById(R.id.img1);
        img = (ImageView) view.findViewById(R.id.img);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        //View view1 = (View) view.findViewById(R.id.view);


        if (position == 0) {
            img1.setVisibility(View.VISIBLE);
        } else {
            img1.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);

        }

        img.setImageResource(images[position]);
        tvName.setText(title[position]);
        tvDesc.setText(desc[position]);




       /* if (isBottom) {

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();

            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            img.setLayoutParams(lp);

        } else {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();

            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            img.setLayoutParams(lp);
        }*/


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
