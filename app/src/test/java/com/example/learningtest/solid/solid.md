# SOLID

SOLID stands for five principles intended to make object-oriented designs more understandable,
flexible, and maintainable.

* SRP: Single Responsibility Principle
* OCP: Open-Closed Principle
* LSP: Liskov Substitution Principle
* ISP: Interface Segregation Principle
* DIP: Dependency Inversion Principle

## SRP(Single Responsibility Principle)

It stands for "A module should have only one responsibility."  
Each module has only one reason to change.

In [this code](SRPViolated.kt) The `LottoSeller` sold lotteries with money.  
But actually it has too many responsibilities:

* It calculates lotteries count with money and price
* It generates lotteries with random numbers
* It validate lotteries

Let's suppose the requirement is changed,
we have to generate the lotto ticket with manual numbers.  
Then LottoSeller class has to be changed.

Let's see this [this code](SRPRefactored.kt).  
`LottoSeller` only calculates the price.  
And it delegate the responsibility for generating Lotto to the `LotteryGenerateStrategy`
Also, the validation for lotto numbers is `Lottery`'s responsibility.

In this example, it says just the SRP for class.  
But it can be applied to the module, package, function in the same way.

## OCP(Open/Closed Principle)

It means that a software entity should be open for extension but closed for modification.  
In simple terms, you should be able to add new features or behaviors without changing existing code,
to avoid breaking things.

Let's suppose the customer have to buy the Lotteries from a LottoSeller.  
This is very simple.

```koltin
private class Customer {
    fun buyLotto(money: Int, lottoSeller, LottoSeller): List<Lottery> {
        return lottoSeller.soldLotto(money)
    }
}
```

But what if there are two kinds of lotto sellers?  
One of the lotto seller is just same with the previous case, but the new one(
`DiscountedLottoSeller`)
sold a lotto for 500 price.

We can do like [this](OCPViolated.kt).

This case violates the OCP.  
The OCP states that "a module should be open for extension but closed for modification,"  
which means you should be able to add new functionality without altering existing code.

We can simply keep the OCP introducing interface [like this](OCPRefactored.kt).

In this example, new seller type(`DiscountedLottoSeller`) is added.  
In [OCPViolated.kt](OCPViolated.kt), we changed the codes in the `Customer` class.  
But In [OCPRefactored.kt](OCPRefactored.kt), we just added a new class and implemented the interface
without changing the existing code.

This is the **good example adhering to the OCP**.

## LSP(Liskov Substitution Principle)

It means that objects of a superclass(or interface) should be **replaceable**  
with objects of a subclass(or implementation) without affecting the correctness of the program.

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

We can refactor this code, [like this](LSPRefactored.kt).  
In this code, the `Square` class is not a subclass of the `Rectangle` class.  
I introduce the `Shape` interface, and the `Rectangle` and `Square` class implement the `Shape`
interface.  
So now, There are no longer fungibility issues (LSP violations).

## ISP(Interface Segregation Principle)

It means that a client should not be forced to implement interfaces they do not use.

LottoSellers starts to chat.  
And there are two kinds of lotto vending machines, a general one and a noisy one.  
These vending machines have a reset function.

Then we can add the features like [this](ISPViolated.kt).  
But the humans such as DiscountedLottoSeller, NormalLottoSeller don't need the reset function.  
And teh vending machines can't chat.  
But DiscountedLottoSeller and NormalLottoSeller are forced to implement the reset function.  
and teh vending machines are forced to implement the chat function.  
Even though they don't need it.

That is, the now codes violate the ISP.

@e violated ISP by forcing human sellers and vending machines to implement methods (`chat` and
`reset`) that were irrelevant to their functionality.
The refactored version resolves this by introducing two specialized abstractions:

* HumanLottoSeller – for sellers with chat capabilities
* MachineLottoSeller – for vending machines with reset capabilities.

So, we can refactor like [this](ISPRefactored.kt).

And, i also introduced the Template Method pattern to reduce the duplicated code.

## DIP(Dependency Inversion Principle)

THe principle states:

* High-level modules should not import anything from low-level modules.  
  Both should depend on abstractions (e.g., interfaces).
* Abstractions should not depend on details.  
  Details (concrete implementations) should depend on abstractions.

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

