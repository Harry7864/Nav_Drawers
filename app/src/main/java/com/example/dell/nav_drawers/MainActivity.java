package com.example.dell.nav_drawers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://yharendra786.000webhostapp.com/mdback.jpg";
    private static final String urlProfileImg = "https://yharendra786.000webhostapp.com/harry1.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Home";
    private static final String TAG_DASHBOARD = "Dashboard";
    private static final String TAG_EMERGENCY = "Directory";
    private static final String TAG_BILL = "Bills";
    private static final String TAG_BALANCESHEET = "Balance sheet";
    private static final String TAG_MEMBERS = "Members";
    private static final String TAG_MYBUILDING = "My Building";
    private static final String TAG_VEHICLES = "Vehicles";
    private static final String TAG_WINGS = "Wins";
    private static final String TAG_PERMISSION = "Permission";
    private static final String TAG_COMPLAIN = "Complain";
    private static final String TAG_RULES = "Rules";
    private static final String TAG_BUILDINGDETAILS = "Building Details";
    private static final String TAG_STATASTICS = "Statastics";
    private static final String TAG_PROFILE = "Profile";
    private static final String TAG_NOTIFICATION = "Notification";
    private static final String TAG_CHANGEPASSWORD = "Change Password";
    private static final String TAG_CONTACTUS = "Contact us";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Harendra Yadav");
        txtWebsite.setText("yharendra7864@gmail.com");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frames, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                return dashBoardFragment;
            case 2:
                BillFragment billFragment = new BillFragment();
                return billFragment;
            case 3:
                EmergencyFragment emergencyFragment = new EmergencyFragment();
                return emergencyFragment;

            case 4:
                BalanceSheetFragment balanceSheetFragment = new BalanceSheetFragment();
                return balanceSheetFragment;
            case 5:
                MembersFragment membersFragment = new MembersFragment();
                Toast.makeText(this, navItemIndex, Toast.LENGTH_SHORT).show();

                return membersFragment;
            case 6:
                MyBuildingsFragment myBuildingsFragment = new MyBuildingsFragment();
                return myBuildingsFragment;
            case 7:
                VehicleFragment vehicleFragment = new VehicleFragment();
                return vehicleFragment;
            case 8:
                WingsFragment wingsFragment = new WingsFragment();
                return wingsFragment;

            case 9:
                PermissionFragment permissionFragment = new PermissionFragment();
                return permissionFragment;
            case 10:
                ComplainsFragment complainsFragment = new ComplainsFragment();
                return complainsFragment;
            case 11:
                RulesFragment rulesFragment = new RulesFragment();
                return rulesFragment;
            case 12:
                BuildingDetailsFragment buildingDetailsFragment = new BuildingDetailsFragment();
                return buildingDetailsFragment;
            case 13:
                StasticsFragment stasticsFragment = new StasticsFragment();
                return stasticsFragment;

            case 14:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 15:
                NotificatonsFragment notificatonsFragment = new NotificatonsFragment();
                return notificatonsFragment;
            case 16:
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                return changePasswordFragment;
            case 17:
                ContactUsFragment contactUsFragment = new ContactUsFragment();
                return contactUsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.gettingstarted:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_deshboard:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_DASHBOARD;
                        break;
                    case R.id.nav_bills:
                        navItemIndex = 2;
                        CURRENT_TAG =TAG_BILL;
                        break;
                    case R.id.nav_emergency:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_EMERGENCY;
                        break;
                    case R.id.nav_balancesheet:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_BALANCESHEET;
                        break;
                    case R.id.nav_members:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_MEMBERS;
                        break;
                    case R.id.nav_building:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_MYBUILDING;
                        break;
                    case R.id.nav_vehicles:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_VEHICLES;
                        break;
                    case R.id.nav_wings:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_WINGS;
                        break;
                    case R.id.nav_permission:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_PERMISSION;
                        break;
                    case R.id.nav_complains:
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_COMPLAIN;
                        break;
                    case R.id.nav_rules:
                        navItemIndex =11;
                        CURRENT_TAG = TAG_RULES;
                        break;
                    case R.id.nav_buildingdetails:
                        navItemIndex = 12;
                        CURRENT_TAG = TAG_BUILDINGDETAILS;
                        break;
                    case R.id.nav_statistics:
                        navItemIndex = 13;
                        CURRENT_TAG = TAG_STATASTICS;
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 14;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.nav_notification:
                        navItemIndex = 15;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        break;
                    case R.id.nav_changee_pass:
                        navItemIndex = 16;
                        CURRENT_TAG = TAG_CHANGEPASSWORD;
                        break;
                    case R.id.nav_contactus:
                        navItemIndex = 17;
                        CURRENT_TAG = TAG_CONTACTUS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
}