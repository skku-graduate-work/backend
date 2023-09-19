package graduationwork.backend.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IngredientInfo {
  private final String name_ko;
  private final String name_en;
  private final String image;
  private final String expirationDate;
  private final Float calories;
  private final Float carbs;
  private final Float fat;
  private final Float protein;

  @Builder
  public IngredientInfo(String name_ko, String name_en, String image, String expirationDate, Float calories, Float carbs, Float fat, Float protein) {
        this.name_ko = name_ko;
        this.name_en = name_en;
        this.image = image;
        this.expirationDate = expirationDate;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }
}
