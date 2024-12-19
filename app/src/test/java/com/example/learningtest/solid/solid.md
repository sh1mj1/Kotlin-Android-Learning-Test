<!-- TOC -->
* [SOLID](#solid)
  * [SRP(Single Responsibility Principle)](#srpsingle-responsibility-principle)
    * [Example of SRP Violation](#example-of-srp-violation)
    * [Example of SRP Adhered(Refactored)](#example-of-srp-adhered--refactored-)
  * [OCP(Open/Closed Principle)](#ocpopenclosed-principle)
    * [Example of OCP Violation](#example-of-ocp-violation)
    * [Example of OCP Adhered(Refactored)](#example-of-ocp-adhered--refactored-)
  * [LSP(Liskov Substitution Principle)](#lspliskov-substitution-principle)
    * [Example of LSP Violation](#example-of-lsp-violation)
    * [Example of LSP Adhered(Refactored)](#example-of-lsp-adhered--refactored-)
  * [ISP(Interface Segregation Principle)](#ispinterface-segregation-principle)
    * [Example of ISP Violation](#example-of-isp-violation)
    * [Example of ISP Adhered(Refactored)](#example-of-isp-adhered--refactored-)
  * [DIP(Dependency Inversion Principle)](#dipdependency-inversion-principle)
    * [Example of DIP Violation](#example-of-dip-violation)
    * [Example of DIP Adhered(Refactored)](#example-of-dip-adhered--refactored-)
<!-- TOC -->

# SOLID

SOLID stands for five principles intended to make object-oriented designs more understandable,
flexible, and maintainable.

* SRP: Single Responsibility Principle
* OCP: Open-Closed Principle
* LSP: Liskov Substitution Principle
* ISP: Interface Segregation Principle
* DIP: Dependency Inversion Principle

## SRP(Single Responsibility Principle)

It stands for **"A module should have only one responsibility"**.  
Each module has **only one reason to change**.

### Example of [SRP Violation](SRPViolated.kt)

In [this code](SRPViolated.kt) The `LottoSeller` sold lotteries with money.  
But actually it has too many responsibilities:

* It calculates lotteries count with money and price
* It generates lotteries with random numbers
* It validate lotteries

What if the lotto numbers have to be generated with the specific strategy?  
Then LottoSeller class has to be changed.  
But Actually the strategy for generating lotto numbers is not the responsibility of the
LottoSeller.  
That is, the LottoSeller class has more than one reason to change.

### Example of [SRP Adhered(Refactored)](SRPRefactored.kt)

Let's see this [this code](SRPRefactored.kt).  
`LottoSeller` only calculates the price.  
And it delegate the responsibility for generating Lotto to the `LotteryGenerateStrategy`
Also, the validation for lotto numbers is `Lottery`'s responsibility.

In this example, it says just the SRP for class.  
But it can be applied to the module, package, function in the same way.

## OCP(Open/Closed Principle)

It means that **"A software entity should be open for extension but closed for modification**".  
In simple terms, you should be able to add new features or behaviors without changing existing code,
to avoid breaking things.

Let's suppose the customer have to buy the Lotteries from a LottoSeller.

```koltin
private class Customer {
    fun buyLotto(money: Int, lottoSeller, LottoSeller): List<Lottery> {
        return lottoSeller.soldLotto(money)
    }
}
```

### Example of [OCP Violation](OCPViolated.kt)

**What if there are two kinds of lotto sellers**?  
We can do like [this](OCPViolated.kt).

One of the lotto seller is just same with the previous case,  
but the new one(`DiscountedLottoSeller`) sold a lotto for 500 price.

This case violates the OCP.  
You should be able to add new functionality without altering existing code.

### Example of [OCP Adhered(Refactored)](OCPRefactored.kt)

We can simply keep the OCP **introducing interface** [like this](OCPRefactored.kt).

In this example, new seller type(`DiscountedLottoSeller`) is added.  
In [OCPViolated.kt](OCPViolated.kt), we changed the codes in the `Customer` class.  
But In [OCPRefactored.kt](OCPRefactored.kt), we just added a new class and implemented the interface
without changing the existing code.

## LSP(Liskov Substitution Principle)

It means that "Objects of a **superclass should be replaceable    
with objects of a subclass(or implementation)** without affecting the correctness of the program."

### Example of [LSP Violation](LSPViolated.kt)

Let's suppose the `Lottery` class has a new requirement.  
It has the rectangle in the `Lottery` class.  
Some special `Lottery` has a `Square`, but some of them have a `Rectangle` which is not a `Square`.

[Look at the Rectangle and Square class in LSPViolated.kt](LSPViolated.kt)

To adhere to the LSP, the `Square` class must be able to substitute the `Rectangle` class.  
[But in this test code](LSPViolatedTest.kt), the `Square` class cannot substitute the `Rectangle`
class.

* Given the `Square` class is a subclass of the `Rectangle` class,
* When we set the width to 2 and the height to 5,
* Then the area should be 10, for adhering to the LSP, but it is 25.
* That means the `Square` class cannot substitute the `Rectangle` class.
* That is the violation of the LSP.

Why does this happen?  
In the first place, the inheritance relationship between the `Square` class and the `Rectangle`
class is wrong.

We have to consider the LSP not only `is-A` when we design the inheritance relationship.  
This example shows that `is-A` is not always enough or right.

### Example of [LSP Adhered(Refactored)](LSPRefactored.kt)

We can refactor this code, [like this](LSPRefactored.kt).  
In this code, the `Square` class is not a subclass of the `Rectangle` class.  
I introduce the `Shape` interface, and the `Rectangle` and `Square` class implement the `Shape`
interface.  
So now, There are no longer substitutability issues (LSP violations).

## ISP(Interface Segregation Principle)

It means that "**A client should not be forced to implement interfaces they do not use**".

Now, the customers can buy lotteries from the human lotto sellers and lotto vending machines.  
Human Lotto Sellers starts to chat.  
Lotto Vending machines have a reset function.

### Example of [ISP Violation](ISPViolated.kt)

We can add the features like [this](ISPViolated.kt).  
But the Human Lotto Sellers don't need the `reset` function.  
And the vending machines can't chat.  
But Human Lotto Sellers are forced to implement the `reset` function.  
and the vending machines are forced to implement the `chat` function.  
Even though they don't need it!

That is, the now codes violate the ISP.

### Example of [ISP Adhered(Refactored)](ISPRefactored.kt)

The refactored version resolves this by introducing two specialized abstractions:

* `HumanLottoSeller` – for sellers with chat capabilities
* `MachineLottoSeller` – for vending machines with reset capabilities.

So, we can refactor like [this](ISPRefactored.kt).

## DIP(Dependency Inversion Principle)

THe principle states:

* **High-level modules should not import anything from low-level modules**.  
  Both should depend on abstractions (e.g., interfaces).
* **Abstractions should not depend on details**.  
  Details (concrete implementations) should depend on abstractions.

### Example of [DIP Violation](DIPViolated.kt)

Let's see this [code](DIPViolated.kt).  
This code violates the DIP.

Why This Violates DIP:

* High-level module (Customer) is tightly coupled to low-level details:
    * Customer depends on the concrete classes `HumanLottoSeller` and `MachineLottoSeller`.
    * If new types of sellers are introduced (e.g., `OnlineLottoSeller`),  
      you would need to modify the `Customer` class.
    * This **violates the Open/Closed Principle** as well.

* Abstractions are not properly utilized:
    * `Customer` should only depend on the abstraction `LottoSeller` and not on specific subclasses.
    * The need to explicitly check the type (is `HumanLottoSeller`, is `MachineLottoSeller`) is a
      red flag for DIP violation.

### Example of [DIP Adhered(Refactored)](DIPRefactored.kt)

Let's see the [refactored code](DIPRefactored.kt).  
In changed code:

* Customer now depends on LottoSeller (an abstraction).
* Polymorphism Resolves Seller Behavior.
* LottoSeller Subclasses Specialize Behavior.

So, we can say that the DIP is adhered to, because:

* High-Level Modules Depend on Abstractions.
* Abstractions Do Not Depend on Details.
