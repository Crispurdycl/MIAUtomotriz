package com.cabroninja.tallermiaumovil.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;// == Clase: HomeViewModel
// == Tipo: class — hereda de ViewModel
// == Rol: Componente de interfaz de usuario.


public class HomeViewModel extends ViewModel {// == Campo: mText (MutableLiveData<String>) — atributo de la clase.


    private final MutableLiveData<String> mText;

// == Método: HomeViewModel
// Parámetros: (sin parámetros)
// Descripción: Realiza la operación asociada al método.
// Retorna: public
    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

// == Método: getText
// Parámetros: (sin parámetros)
// Descripción: Obtiene un valor o recurso.
// Retorna: LiveData<String>
    public LiveData<String> getText() {
        return mText;
    }
}