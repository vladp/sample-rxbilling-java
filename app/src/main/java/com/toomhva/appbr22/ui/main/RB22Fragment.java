package com.toomhva.appbr22.ui.main;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.toomhva.appbr22.R;


import com.gen.rxbilling.client.RxBilling;
import com.gen.rxbilling.client.RxBillingImpl;
import com.gen.rxbilling.connection.BillingClientFactory;
import com.gen.rxbilling.connection.RepeatConnectionTransformer;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


/**
 * A placeholder fragment containing a simple view.
 */
public class RB22Fragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;

    RxBilling rxBilling;
    CompositeDisposable disposable;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index); // this will create a notification through modelview


        BillingClientFactory billingClientFactory =
                new BillingClientFactory(this.getContext(), new RepeatConnectionTransformer<>());
        rxBilling = new RxBillingImpl(billingClientFactory);
        disposable = new CompositeDisposable();
        Timber.d("--->onCreate");
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View nothingView = inflater.inflate(R.layout.plaintext__fragment__1fld,
                container, false);
        TextView tv=
                (TextView)  nothingView.findViewById(R.id.plaintext__fragment__textView2);
        tv.setText(" some text");
        Timber.d("--->onCreateView");
        return nothingView;
    }

    @Override
    public void onStart() {
        super.onStart();
        disposable.add(rxBilling.observeUpdates()
                .subscribe((//PurchasesUpdate
                            it) -> {
                            final String x = it.toString();
                            Timber.d("no err: %s ",x);
                        },
                        (err) -> {
                            Timber.e(err);
                        }
                )
        );
        Timber.d("--->onStart");
    }
    @Override
    public void onPause () {
        super.onPause();
        Timber.d("--->onPause");
    }


    @Override
    public void onStop () {
        if (disposable!=null && disposable.isDisposed()==false) {
            disposable.clear();        }
        super.onStop();
        Timber.d("--->onStop");
    }
}