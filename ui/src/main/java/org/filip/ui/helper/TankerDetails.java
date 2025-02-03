package org.filip.ui.helper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TankerDetails implements Serializable
{
    private int tankerNumber;
    private boolean isReadyToServe;

    public TankerDetails(int tankerNumber, boolean isReadyToServe) {
        this.tankerNumber = tankerNumber;
        this.isReadyToServe = isReadyToServe;
    }
}
