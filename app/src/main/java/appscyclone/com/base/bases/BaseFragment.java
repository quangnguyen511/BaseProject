package appscyclone.com.base.bases;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import appscyclone.com.base.R;
import appscyclone.com.base.interfaces.ApiResponseListener;
import appscyclone.com.base.interfaces.FragmentResultListener;
import appscyclone.com.base.models.ErrorModel;
import appscyclone.com.base.network.builder.ApiBaseHandler;
import appscyclone.com.base.network.builder.ApiClient;

/*
 * Created by NhatHoang on 20/04/2018.
 */
public abstract class BaseFragment extends Fragment implements ApiResponseListener {

    private FragmentResultListener resultListener;
    private int mCodeRequest;
    public ApiClient mApiClient;
    private ApiBaseHandler mBaseHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseHandler = new ApiBaseHandler((BaseActivity) getActivity(), this);
        mApiClient = new ApiClient(mBaseHandler.requestListener, (BaseApplication) getActivity().getApplication());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getContext() != null)
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (((BaseApplication) getActivity().getApplication()).getCancelAnimation()) {
            nextAnim = enter ? android.R.anim.fade_in : android.R.anim.fade_out;
        }
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }
        if (animation != null && getView() != null) {
            getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (getView() != null)
                        getView().setClickable(true);
                }

                public void onAnimationEnd(Animation animation) {
                    if (getView() != null)
                        getView().setClickable(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

            });
        }
        return animation;
    }

    public void setActionBarTitle(View view, String title) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setActionBarTitle(view, title);
        }
    }

    public FragmentResultListener getResultListener() {
        return resultListener;
    }

    public void setFragmentForResult(int codeRequest, FragmentResultListener fragmentResultListener) {
        this.resultListener = fragmentResultListener;
        this.mCodeRequest = codeRequest;
    }

    protected void callBackFragmentResult(Bundle bundle) {
        if (resultListener != null)
            resultListener.onFragmentForResult(mCodeRequest, bundle);
    }

    protected void callBackFragmentResult(int code) {
        if (resultListener != null)
            resultListener.onFragmentForResult(code, new Bundle());
    }

    public void clearAllBackStack() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).clearAllBackStack();
        }
    }

    public void replaceFragment(BaseFragment fragment, boolean isAddToBackStack, boolean isAnimation) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).replaceFragment(fragment, isAddToBackStack, isAnimation);
        }
    }

    public void addFragment(BaseFragment fragment, boolean isAddToBackStack, boolean isAnimation) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).addFragment(fragment, isAddToBackStack, isAnimation);
        }
    }

    @Override
    public void onDataError(int nCode, ErrorModel t) {

    }

    @Override
    public void onDataResponse(int nCode, BaseModel nData) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaseHandler.setResponseEnable(false);
    }
}
