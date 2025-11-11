package com.cabroninja.tallermiaumovil.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;// == Clase: GalleryViewModel
// == Tipo: class — hereda de ViewModel
// == Rol: Componente de interfaz de usuario.


public class GalleryViewModel extends ViewModel {// == Campo: mText (MutableLiveData<String>) — atributo de la clase.


    private final MutableLiveData<String> mText;

// == Método: GalleryViewModel
// Parámetros: (sin parámetros)
// Descripción: Realiza la operación asociada al método.
// Retorna: public
    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

// == Método: getText
// Parámetros: (sin parámetros)
// Descripción: Obtiene un valor o recurso.
// Retorna: LiveData<String>
    public LiveData<String> getText() {
        return mText;
    }
}