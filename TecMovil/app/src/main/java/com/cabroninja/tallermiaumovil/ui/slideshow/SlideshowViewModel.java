package com.cabroninja.tallermiaumovil.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;// == Clase: SlideshowViewModel
// == Tipo: class — hereda de ViewModel
// == Rol: Componente de interfaz de usuario.


public class SlideshowViewModel extends ViewModel {// == Campo: mText (MutableLiveData<String>) — atributo de la clase.


    private final MutableLiveData<String> mText;

// == Método: SlideshowViewModel
// Parámetros: (sin parámetros)
// Descripción: Realiza la operación asociada al método.
// Retorna: public
    public SlideshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

// == Método: getText
// Parámetros: (sin parámetros)
// Descripción: Obtiene un valor o recurso.
// Retorna: LiveData<String>
    public LiveData<String> getText() {
        return mText;
    }
}