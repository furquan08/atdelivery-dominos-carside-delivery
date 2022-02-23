@fulfillment-service-regression-test
Feature: Validate Domino's Carside Delivery feature in fulfillment  service

  @fs-carryout
  Scenario: Validate Carryout DCD end-to-end happy path scenario
    When I published DUC order placed message for a "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_PLACED"
    When I hit SEBS with "OrderAccepted" status for "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_ACCEPTED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_ACCEPTED"
    When I hit SEBS with "OrderOnMakeLine" status for "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_ON_MAKE_LINE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_ON_MAKE_LINE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "ORDER_PLACED"
    When I hit power to checkIn as "ECOMM" customer
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_ARRIVED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "CUSTOMER_ARRIVED"
    When I hit SEBS with "OrderInOven" status for "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_IN_OVEN"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_IN_OVEN"
    When I hit SEBS with "OrderReady" status for "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_READY"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_READY"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_WAITING"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "CUSTOMER_WAITING"
    When I set DUC order status as "TEAM_MEMBER_OUT"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "TEAM_MEMBER_OUT"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "TEAM_MEMBER_OUT"
    When I set DUC order status as "COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "COMPLETE"
    When I hit SEBS with "OrderComplete" status for "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_COMPLETE"

  @fs-carryout
  Scenario: Validate DUC_CLEAR_ORDER state for carryout DCD order.
    When I do price order call for a "Carryout" order
    Then I get payment amount
    When I do place order call for "ECOMM-Carryout" order
    Then I validate that order was created for "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "ORDER_PLACED"
    When I set DUC order status as "DUC_CLEAR_ORDER"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "DUC_CLEAR_ORDER"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "DUC_CLEAR_ORDER"

  @fs-carryout
  Scenario: Validating AUTO_COMPLETE scenario for carryout DCD order.
    When I do price order call for a "Carryout" order
    Then I get payment amount
    When I do place order call for "APP-Carryout" order
    Then I validate that order was created for "Carryout" order
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "ORDER_PLACED"
    When I hit power to checkIn as "AUTOCHECKIN" customer
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_WAITING"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "CUSTOMER_WAITING"
    When I set DUC order status as "TEAM_MEMBER_OUT"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "TEAM_MEMBER_OUT"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "TEAM_MEMBER_OUT"
    When I waited for 10 minutes with no further activity
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "COMPLETE"

  @fs-carside
  Scenario: Validate Carside DCD end-to-end happy path scenario
    When I published DUC order placed message for a "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_PLACED"
    When I hit SEBS with "OrderAccepted" status for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_ACCEPTED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_ACCEPTED"
    When I hit SEBS with "OrderOnMakeLine" status for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_ON_MAKE_LINE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_ON_MAKE_LINE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "ORDER_PLACED"
    When I hit power to checkIn as "ECOMM" customer
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_ARRIVED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "CUSTOMER_ARRIVED"
    When I hit SEBS with "OrderInOven" status for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_IN_OVEN"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_IN_OVEN"
    When I hit SEBS with "OrderReady" status for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_READY"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_READY"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_WAITING"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "CUSTOMER_WAITING"
    When I hit SEBS with "OrderDispatched" status for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_DISPATCHED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_DISPATCHED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "TEAM_MEMBER_OUT"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "TEAM_MEMBER_OUT"
    When I set DUC order status as "COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "COMPLETE"
    When I hit SEBS with "OrderComplete" status for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_COMPLETE"
    When I hit SEBS with "OrderCancelled" status for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "FLATTENED_ORDER" event as "ORDER_CANCELLED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_ORDER" event as "ORDER_CANCELLED"

  @fs-carside
  Scenario: Validate DUC_CLEAR_ORDER state for carside DCD order.
    When I do price order call for a "Carside" order
    Then I get payment amount
    When I do place order call for "ECOMM-Carside" order
    Then I validate that order was created for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "ORDER_PLACED"
    When I set DUC order status as "DUC_CLEAR_ORDER"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "DUC_CLEAR_ORDER"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "DUC_CLEAR_ORDER"

  @fs-carside
  Scenario: Validating AUTO_COMPLETE scenario for carside DCD order.
    When I do price order call for a "Carside" order
    Then I get payment amount
    When I do place order call for "ECOMM-Carside" order
    Then I validate that order was created for "Carside" order
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "ORDER_PLACED"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "ORDER_PLACED"
    When I hit power to checkIn as "ECOMM" customer
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "CUSTOMER_WAITING"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "CUSTOMER_WAITING"
    When I dispatch carside order with available team members
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "TEAM_MEMBER_OUT"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "TEAM_MEMBER_OUT"
    When I waited for 10 minutes with no further activity
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT" event as "COMPLETE"
    Then I verify DUC order status in "fulfillment-service" for "DRIVE_UP_CARRYOUT_STATUS" event as "COMPLETE"

