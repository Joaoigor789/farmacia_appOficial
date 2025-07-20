package com.example.farmacia_app.desativados;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.farmacia_app.R;

import java.util.List;

public class VideoPagerAdapter extends RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder> {
    private final Context context;
    private final List<Uri> videoUris;

    public VideoPagerAdapter(Context context, List<Uri> videoUris) {
        this.context = context;
        this.videoUris = videoUris;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        WebView webView = holder.webView;
        SeekBar seekBar = holder.seekBar;

        // Carregar o vídeo no WebView
        String videoUrl = videoUris.get(position).toString();
        webView.loadUrl(videoUrl);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        // O WebView não tem suporte direto para controles como SeekBar,
        // você pode adicionar a lógica de controle de progresso se necessário.

        // Exemplo simples de detectar eventos de navegação (não implementa o SeekBar diretamente):
        webView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();  // Garantir que o evento de clique seja tratado corretamente
            }

            GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // Aqui você pode implementar funcionalidades adicionais, como pausar ou reiniciar o vídeo
                    return true;
                }
            });
            return gestureDetector.onTouchEvent(event);
        });

        // Se você quiser usar o SeekBar para controlar o progresso do vídeo, você pode adicionar essa lógica aqui.
        // Por exemplo, ao usar o SeekBar para controlar a posição do vídeo, implemente a lógica do listener do SeekBar.
    }

    @Override
    public int getItemCount() {
        return videoUris.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView webView;
        SeekBar seekBar;

        public VideoViewHolder(View view) {
            super(view);
              // Usando o WebView no layout
            seekBar = view.findViewById(R.id.seek_bar);      // A SeekBar pode ser mantida se precisar de controle adicional
        }
    }
}
