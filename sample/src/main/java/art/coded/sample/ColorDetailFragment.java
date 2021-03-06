package art.coded.sample;

import android.app.Activity;

import art.coded.sample.data.ColorData;
import art.coded.sample.data.ColorData.ColorItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A fragment representing a single ColorItem detail screen.
 * This fragment is either contained in a {@link ColorListActivity}
 * in two-pane mode (on tablets) or a {@link ColorDetailActivity}
 * in two-pane mode (on tablets) or a {@link ColorDetailActivity}
 * on handsets.
 */
public class ColorDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    static final String ARG_ITEM_ID = "item_id";

    /**
     * The color content this fragment is presenting.
     */
    private ColorItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ColorDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_ITEM_ID)) {
            // Load the color content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String id = arguments.getString(ARG_ITEM_ID);
            if (id != null) {
                ColorItem rateable
                        = ColorData.getSavedItems().get(Integer.parseInt(id));
                if (rateable != null) mItem = rateable.getObject();
            }

            Activity activity = this.getActivity();
            if (activity == null) return;
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getContent());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.color_detail, container, false);

        // Show the color content as text in a TextView.
        if (mItem != null) {
            int color = getResources().getColor(mItem.getColorRes());
            container.setBackgroundColor(color);
            ((TextView) rootView.findViewById(R.id.color_detail))
                    .setText(String.format(
                        "%s rated %s\n\n%s",
                        mItem.colorResToString(getContext()),
                        NumberFormat.getPercentInstance().format(mItem.getPercent()),
                        mItem.getDetails()
                    ));
        } return rootView;
    }
}
