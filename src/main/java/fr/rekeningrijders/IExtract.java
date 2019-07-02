package fr.rekeningrijders;

public interface IExtract<T1, T2>
{
    T2 extract(T1 subject);
}