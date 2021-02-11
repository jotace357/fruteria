package com.bit.fruteria;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping(value = "/frutas")
public class FruteriaRestController {
    private List<Fruta> frutas = new ArrayList<>();

    @GetMapping
    public ResponseEntity index() {
        System.out.println(frutas.toString());
        return ResponseEntity.ok(frutas);
    }

    @GetMapping("/{id}")
    public ResponseEntity getFruta(@PathVariable(value="id") int id) {
        Optional<Fruta> fruta = frutas.stream().filter(f -> f.getId() == id).findFirst();

        if(fruta.isPresent()){
            return ResponseEntity.ok(fruta.get());
        }else {
            return ResponseEntity.badRequest().body("No se encontro la fruta con id " + id);
        }
    }

    @PostMapping
    public ResponseEntity addFruta(@RequestParam(value="nombre") String name, @RequestParam(value="descripcion") String desc, @RequestParam(value="url") String url) {
        OptionalInt maxOpt = frutas.stream().mapToInt(f -> f.getId()).max();
        int max = maxOpt.isPresent()? maxOpt.getAsInt()+1 : 1;
        Fruta nuevaFruta = new Fruta(max,name, desc, url);
        frutas.add(nuevaFruta);
        return ResponseEntity.ok(nuevaFruta);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateFruta(@RequestParam(value="nombre") String name, @PathVariable(value="id") int id, @RequestParam(value="descripcion") String desc, @RequestParam(value="url") String url) {
        Optional<Fruta> optionalFruta = frutas.stream().filter(f -> f.getId() == id).findFirst();
        if(!optionalFruta.isPresent()){
            return ResponseEntity.badRequest().body("No se encontro la fruta con id " + id);
        }

        Fruta fruta = optionalFruta.get();
        fruta.setNombre(name);
        fruta.setDescripcion(desc);
        fruta.setUrlImagen(url);

        return ResponseEntity.ok(fruta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removeFruta(@PathVariable(value="id") int id) {
        Optional<Fruta> optionalFruta = frutas.stream().filter(f -> f.getId() == id).findFirst();
        if(!optionalFruta.isPresent()){
            return ResponseEntity.badRequest().body("No se encontro la fruta con id " + id);
        }
        frutas.remove(optionalFruta.get());
        return ResponseEntity.noContent().build();
    }
}
