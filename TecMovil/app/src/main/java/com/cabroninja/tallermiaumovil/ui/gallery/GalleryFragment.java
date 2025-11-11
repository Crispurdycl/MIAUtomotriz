package com.cabroninja.tallermiaumovil.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cabroninja.tallermiaumovil.databinding.FragmentGalleryBinding;// == Clase: GalleryFragment
// == Tipo: class — hereda de Fragment
// == Rol: Componente de interfaz de usuario.


public class GalleryFragment extends Fragment {// == Campo: binding (FragmentGalleryBinding) — atributo de la clase.


    private FragmentGalleryBinding binding;

// == Método: onCreateView
// Parámetros: inflater (LayoutInflater), container (ViewGroup), savedInstanceState (Bundle)
// Descripción: Callback del ciclo de vida de Android.
// Retorna: View
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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