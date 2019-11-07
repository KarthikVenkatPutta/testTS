Feature: Shop on Ebay
  Scenario:Search and Add Top 5 items in to cart
  #  Given Sample test
    Given I have logged into ebay
    And Search for a Product
    And i add top 5 listings to cart
    Then i proceed to checkout cart