package vfa.vfdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vfa.vfdemo.fragments.FragPager;
import vfa.vfdemo.fragments.FragSlideMenu;
import vfa.vfdemo.fragments.images.FragGallery;
import vfa.vfdemo.utils.ViewHelper;
import vfa.vflib.activity.VFActivity;


public class ActivitySlideMenu extends VFActivity {
    private DrawerLayout drawerLayout;
    private View viewMenuContainer;

    private ViewGroup viewActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);
        setHomeActionBar();
    }

    public void setUpLayout(){
        viewMenuContainer = findViewById(R.id.fragMenuContainer);
        setFragmentContainerId(R.id.fragContainer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setUpLayout();
    }

    public void setSlideMenuFagment(Fragment fg){
        startFragment(fg,R.id.fragMenuContainer);
    }

    public void onSlideMenuOpen(){

    }

    public void onSlideMenuClose(){

    }

    public void toggSlideMenu(){
        if(drawerLayout.isDrawerOpen(viewMenuContainer)){
            drawerLayout.closeDrawer(viewMenuContainer);
        }else {
            drawerLayout.openDrawer(viewMenuContainer);
        }
    }

    public void onSlideMenuSelected(int menuItemIndex){

    }

    public void setHomeActionBar(){
        viewActionBar = ViewHelper.getViewGroup(this,R.layout.actionbar_root);
        viewActionBar.findViewById(R.id.buttonMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggSlideMenu();
            }
        });

        setupActionBarView(viewActionBar);
    }
    public void setActionBarRightContent(View viewContent){
        ViewGroup viewRight = (ViewGroup) viewActionBar.findViewById(R.id.viewRightContent);
        viewRight.removeAllViews();
        viewRight.addView(viewContent);

//        viewRight.setBackgroundColor(Color.CYAN);
//        setupActionBarView(viewActionBar);
//        setHomeActionBar();
    }

    public void setActionBarText(String text){
        TextView tvTitle = (TextView) viewActionBar.findViewById(R.id.tvActionBarText);
        if(tvTitle != null){
            tvTitle.setText(text);
        }
    }
}
