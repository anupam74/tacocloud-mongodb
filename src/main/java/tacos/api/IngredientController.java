package tacos.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

@RestController
@RequestMapping(path="/ingredients", produces="application/json")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public Flux<Ingredient> allIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping ("/recent")
    public Flux<Ingredient> last3Ingredients() {
        return ingredientRepository.findAll().take(3);
    }

    @GetMapping("/{id}")
    public Mono<Ingredient> findIngredient(@PathVariable String id) {
        return ingredientRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<Ingredient> updateIngredient(@PathVariable String id, @RequestBody Ingredient ingredient) {
        if (ingredient.getId().equals(id)) {
            return ingredientRepository.save(ingredient);
        } else {
            throw new IllegalStateException("Ingredient id provided does not match");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Ingredient> createIngredient(@RequestBody Mono<Ingredient> ingredientMono) {
        return ingredientRepository.saveAll(ingredientMono).next();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteIngredient(@PathVariable String id) {
        return ingredientRepository.deleteById(id);
    }

}

