package jp.co.interplan.bookkeeper;


import static jp.co.interplan.bookkeeper.DLog.dlog;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class Scanner {
    GmsBarcodeScannerOptions options;
    GmsBarcodeScanner scanner;
    OnSuccessListener<Barcode> listener;
    IScanner callback;

    public Scanner(Context context, IScanner callback) {
        this.callback = callback;
        options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .enableAutoZoom()
                .build();

        scanner = GmsBarcodeScanning.getClient(context, options);
        listener = o -> {
            if (o != null) {
                String isbn = o.getRawValue();
                if (isbn.length() > 0 && callback != null) {
                    this.callback.onRead(isbn);
                }
            }
        };
    }

    public void start() {
        if (listener != null) {
            scanner.startScan().addOnSuccessListener(listener)
                    .addOnCanceledListener(() -> dlog("cancelled"))
                    .addOnFailureListener((e) -> dlog("error:" + e));
        }
    }
}

