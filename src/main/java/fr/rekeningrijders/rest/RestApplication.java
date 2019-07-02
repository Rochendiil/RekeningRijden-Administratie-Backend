package fr.rekeningrijders.rest;

import lombok.NoArgsConstructor;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
@NoArgsConstructor
public class RestApplication extends Application
{
}