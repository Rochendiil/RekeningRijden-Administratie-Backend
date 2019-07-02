package fr.rekeningrijders.models.pojo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuelType
{
    public static final byte UNKNOWN = 0;
    public static final byte BENZINE = 1;
    public static final byte DIESEL = 2;
    public static final byte ELECTRIC = 3;
    public static final byte GAS = 4;
    public static final byte HYDROGEN = 5;
    public static final byte OTHER = 6;

    public static final byte MAX_VALUE = OTHER;
}