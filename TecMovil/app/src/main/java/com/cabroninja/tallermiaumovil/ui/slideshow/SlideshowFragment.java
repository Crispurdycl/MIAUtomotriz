package com.cabroninja.tallermiaumovil.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cabroninja.tallermiaumovil.databinding.FragmentSlideshowBinding;// == Clase: SlideshowFragment
// == Tipo: class — hereda de Fragment
// == Rol: Componente de interfaz de usuario.


public class SlideshowFragment extends Fragment {// == Campo: binding (FragmentSlideshowBinding) — atributo de la clase.


    private FragmentSlideshowBinding binding;

// == Método: onCreateView
// Parámetros: inflater (LayoutInflater), container (ViewGroup), savedInstanceState (Bundle)
// Descripción: Callback del ciclo de vida de Android.
// Retorna: View
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
// == Método: onDestroyView
// Parámetros: (sin parámetros)
// Descripción: Realiza la operación asociada al método.
// Retorna: void
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}