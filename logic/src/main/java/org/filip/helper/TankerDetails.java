package org.filip.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TankerDetails
{
    private int tankerNumber;
    private boolean isReadyToServe;

    public TankerDetails(int tankerNumber, boolean isReadyToServe) {
        this.tankerNumber = tankerNumber;
        this.isReadyToServe = isReadyToServe;
    }
}
