@delivery-presentation-service-regression-test

Feature: Validate DUC states in delivery presentation Service

  @dps-carryout
  @delivery-presentation-service-smoke-test
  Scenario: Validating Carryout DCD order.
    When I do price order call for a "Carryout" order
    Then I get payment amount
    When I do place order call for "ECOMM-Carryout" order
    Then I validate that order was created for "Carryout" order
    Then I hit service to get DUC state of an order
      And I validate DUC State as ORDER_PLACED
    When I hit power to checkIn as "ECOMM" customer
    Then I hit service to get DUC state of an order
      And I validate DUC State as CUSTOMER_WAITING
    When I set DUC order status as "TEAM_MEMBER_OUT"
    Then I hit service to get DUC state of an order
      And I validate DUC State as TEAM_MEMBER_OUT
    When I set DUC order status as "COMPLETE"
    Then I hit service to get DUC state of an order
      And I validate DUC State as COMPLETE

  @dps-carryout
  @delivery-presentation-service-smoke-test
  Scenario: Validating DCD negative scenario
    When I do price order call for a "Carryout" order
    Then I get payment amount
    When I do place order call for "CASH-Carryout" order
    Then I validate that order was created for "CASH" order
    Then I hit service to get DUC state of an order

  @dps-carryout
  @DriveUpCarryout
  Scenario: Validate DUC (DRIVE_UP_CARRYOUT) states for carryout DCD order.
    When I do price order call for a "Carryout" order
    Then I get payment amount
    When I do place order call for "APP-Carryout" order
    Then I validate that order was created for "Carryout" order
    When I hit service to get DUC state of an order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    When I hit power to checkIn as "AUTOCHECKIN" customer
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_WAITING"
    When I set DUC order status as "TEAM_MEMBER_OUT"
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "TEAM_MEMBER_OUT"
    When I set DUC order status as "COMPLETE"
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "COMPLETE"

  @dps-carryout
  @delivery-presentation-service-smoke-test
  @DriveUpCarryoutOrder
  @PayPalOrder
  Scenario: Validate DUC (DRIVE_UP_CARRYOUT_ORDER) states for carryout DCD order
    When I published DUC order placed message for a "Carryout" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderPlaced"
    When I hit SEBS with "OrderAccepted" status for "Carryout" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderAccepted"
    When I hit SEBS with "OrderOnMakeLine" status for "Carryout" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderOnMakeLine"
    When I hit service to get DUC state of an order
    Then I validate DUC State as ORDER_PLACED
    When I hit power to checkIn as "ECOMM" customer
    Then I hit service to get DUC state of an order
    And I validate DUC State as CUSTOMER_ARRIVED
    When I hit SEBS with "OrderInOven" status for "Carryout" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderInOven"
    When I hit SEBS with "OrderReady" status for "Carryout" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderReady"
    Then I hit service to get DUC state of an order
      And I validate DUC State as CUSTOMER_WAITING
    When I set DUC order status as "TEAM_MEMBER_OUT"
    Then I hit service to get DUC state of an order
      And I validate DUC State as TEAM_MEMBER_OUT
    When I hit SEBS with "OrderComplete" status for "Carryout" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderComplete"
    When I set DUC order status as "COMPLETE"
    When I hit service to get DUC state of an order
      And I validate DUC State as COMPLETE
    When I hit SEBS with "OrderCancelled" status for "Carryout" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderCancelled"

  @dps-carryout
  @PhoneOrder
  Scenario: Validating Phone order for DCD
    When I hit SEBS with "OrderAccepted" status for "PHONE" order
    Then I hit service to get DUC state of an order
    And I validate DUC State as DUC_ELIGIBLE
    When I hit power to checkIn as "PHONE" customer
    Then I hit service to get DUC state of an order
      And I validate DUC State as CUSTOMER_ARRIVED
    And I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderAccepted"
    When I hit SEBS with "OrderOnMakeLine" status for "PHONE" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderOnMakeLine"
    When I hit SEBS with "OrderInOven" status for "PHONE" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderInOven"
    When I hit SEBS with "OrderReady" status for "PHONE" order
    When I hit service to get DUC state of an order
    Then I validate DUC State as CUSTOMER_WAITING
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderReady"
    When I set DUC order status as "TEAM_MEMBER_OUT"
    Then I hit service to get DUC state of an order
      And I validate DUC State as TEAM_MEMBER_OUT
    When I set DUC order status as "COMPLETE"
    Then I hit service to get DUC state of an order
      And I validate DUC State as COMPLETE

  @dps-carryout
  @AutoComplete
  Scenario: Validating AUTO_COMPLETE scenario for carryout DCD order.
    When I do price order call for a "Carryout" order
    Then I get payment amount
    When I do place order call for "ECOMM-Carryout" order
    Then I validate that order was created for "Carryout" order
    Then I hit service to get DUC state of an order
      And I validate DUC State as ORDER_PLACED
    When I hit power to checkIn as "ECOMM" customer
    Then I hit service to get DUC state of an order
      And I validate DUC State as CUSTOMER_WAITING
    When I set DUC order status as "TEAM_MEMBER_OUT"
    Then I hit service to get DUC state of an order
      And I validate DUC State as TEAM_MEMBER_OUT
    When I waited for 10 minutes with no further activity
    Then I hit service to get DUC state of an order
      And I validate DUC State as COMPLETE

  @dps-carside
  @delivery-presentation-service-smoke-test
  Scenario: Validating Carside DCD order.
    When I do price order call for a "Carside" order
    Then I get payment amount
    When I do place order call for "ECOMM-Carside" order
    Then I validate that order was created for "Carside" order
    Then I hit service to get DUC state of an order
      And I validate DUC State as ORDER_PLACED
    When I hit power to checkIn as "ECOMM" customer
    Then I hit service to get DUC state of an order
      And I validate DUC State as CUSTOMER_WAITING
      And I validate carside team members list
    When I dispatch carside order with available team members
    Then I hit service to get DUC state of an order
    And I validate DUC State as TEAM_MEMBER_OUT
    When I set DUC order status as "COMPLETE"
    Then I hit service to get DUC state of an order
    And I validate DUC State as COMPLETE

  @dps-carside
  @DriveUpCarryout
  Scenario: Validate DUC (DRIVE_UP_CARRYOUT) states for carside DCD order.
    When I do price order call for a "Carside" order
    Then I get payment amount
    When I do place order call for "APP-Carside" order
    Then I validate that order was created for "Carside" order
    When I hit service to get DUC state of an order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    When I hit power to checkIn as "AUTOCHECKIN" customer
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_WAITING"
      And I validate carside team members list
    When I dispatch carside order with available team members
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "TEAM_MEMBER_OUT"
    When I set DUC order status as "COMPLETE"
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT" event as "COMPLETE"

  @dps-carside
  @delivery-presentation-service-smoke-test
  @DriveUpCarryoutOrder
  @PayPalOrder
  Scenario: Validate DUC (DRIVE_UP_CARRYOUT_ORDER) states for carside DCD order.
    When I published DUC order placed message for a "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderPlaced"
    When I hit SEBS with "OrderAccepted" status for "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderAccepted"
    When I hit SEBS with "OrderOnMakeLine" status for "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderOnMakeLine"
    When I hit SEBS with "OrderInOven" status for "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderInOven"
    When I hit service to get DUC state of an order
    Then I validate DUC State as ORDER_PLACED
    When I hit power to checkIn as "ECOMM" customer
    Then I hit service to get DUC state of an order
    And I validate DUC State as CUSTOMER_ARRIVED
    When I hit SEBS with "OrderReady" status for "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderReady"
    Then I hit service to get DUC state of an order
      And I validate DUC State as CUSTOMER_WAITING
    When I hit SEBS with "OrderDispatched" status for "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderDispatched"
    When I hit service to get DUC state of an order
      And I validate DUC State as TEAM_MEMBER_OUT
    When I hit SEBS with "OrderComplete" status for "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderComplete"
    When I set DUC order status as "COMPLETE"
    When I hit service to get DUC state of an order
      And I validate DUC State as COMPLETE
    When I hit SEBS with "OrderCancelled" status for "Carside" order
    Then I verify DUC order status in "delivery-presentation-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "OrderCancelled"

  @dps-carside
  @AutoComplete
  Scenario: Validating AUTO_COMPLETE scenario for carside DCD order.
    When I do price order call for a "Carside" order
    Then I get payment amount
    When I do place order call for "ECOMM-Carside" order
    Then I validate that order was created for "Carside" order
    Then I hit service to get DUC state of an order
      And I validate DUC State as ORDER_PLACED
    When I hit power to checkIn as "ECOMM" customer
    Then I hit service to get DUC state of an order
      And I validate DUC State as CUSTOMER_WAITING
      And I validate carside team members list
    When I dispatch carside order with available team members
    Then I hit service to get DUC state of an order
      And I validate DUC State as TEAM_MEMBER_OUT
    When I waited for 10 minutes with no further activity
    Then I hit service to get DUC state of an order
      And I validate DUC State as COMPLETE