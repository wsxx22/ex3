package com.hateos.demo;

import com.hateos.demo.model.Color;
import com.hateos.demo.model.Vehicle;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@RestController
@RequestMapping(path = "/vehicle", produces = {MediaType.APPLICATION_JSON_VALUE})
public class VehicleApi {

    private List<Vehicle> vehicleList;

    public VehicleApi() {
        this.vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle(1L, "Audi", "A6", Color.RED));
        vehicleList.add(new Vehicle(2L, "BMW", "E46", Color.GREEN));
        vehicleList.add(new Vehicle(3L, "Pagani Zonda", "C12S", Color.BLUE));
    }

    //    wyświetlenie listy pojazdów
    @GetMapping
    public ResponseEntity<Resources<Vehicle>> getVehicles(){
        vehicleList.forEach(vehicle -> vehicle.add(linkTo(VehicleApi.class).slash(vehicle.getId()).withSelfRel()));
        Link link = linkTo(VehicleApi.class).withSelfRel();

        Resources<Vehicle> resources = new Resources<>(vehicleList, link);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    //    wyświetlenie pojazdu o określonym id
    @GetMapping(path="/{id}")
    public ResponseEntity<Resource<Vehicle>> getVehicleById(@PathVariable long id){
        return vehicleList.stream()
                .filter(vehicle -> vehicle.getVehicleId() == id).findFirst()
                .map(vehicle -> {
                    Link link = linkTo(VehicleApi.class).slash(id).withSelfRel();
                    Resource<Vehicle> entityModel = new Resource<>(vehicle, link);
                    return new ResponseEntity<>(entityModel, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //    wybór pojazu po kolorze
    @GetMapping(path="/color")
    public ResponseEntity<List<Vehicle>> showByColor(@RequestParam Color color){
        List<Vehicle> v = vehicleList.stream().filter(vehicle -> vehicle.getColor() == color).collect(Collectors.toList());
        return new ResponseEntity<>(v, HttpStatus.OK);
    }


    //    dodanie pojazdu
    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle){
        if(vehicleList.add(vehicle)){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //    modyfikowanie pojazdu
    @PutMapping
    public ResponseEntity<Vehicle> modVehicle(@RequestBody Vehicle newVehicle){
        Optional<Vehicle> first = vehicleList.stream().filter(vehicle -> vehicle.getVehicleId() == newVehicle.getVehicleId()).findFirst();
        if(first.isPresent()){
            vehicleList.remove(first.get());
            vehicleList.add(newVehicle);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    modyfikowanie jednego z pól pozycji
    @RequestMapping(method=RequestMethod.PATCH)
    public ResponseEntity<Vehicle> modFiledVehicle(@RequestBody Vehicle newVehicle){
        Optional<Vehicle> first = vehicleList.stream().filter(vehicle -> vehicle.getVehicleId() == newVehicle.getVehicleId()).findFirst();
        if (first.isPresent()) {
            if (!newVehicle.getMark().equals("")) {
                first.get().setMark(newVehicle.getMark());
            }
            if (!newVehicle.getModel().equals("")) {
                first.get().setModel(newVehicle.getModel());
            }
            if (!newVehicle.getColor().equals("")) {
                first.get().setColor(newVehicle.getColor());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //    usuwanie pojazdu o określonym id
    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> deleteVehicle(@PathVariable long id){
        Optional<Vehicle> first = vehicleList.stream().filter(vehicle -> vehicle.getVehicleId() == id).findFirst();
        if(first.isPresent()){
            vehicleList.remove(first.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
