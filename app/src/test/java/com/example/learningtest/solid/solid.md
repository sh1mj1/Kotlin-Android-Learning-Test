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
[But in this test code](LSPViolatedTest.kt), the `Square` class cannot substitute the `Rectangle` class.  

* Given the `Square` class is a subclass of the `Rectangle` class,  
* When we set the width to 2 and the height to 5,  
* Then the area should be 10, for adhering to the LSP, but it is 25.  
* That means the `Square` class cannot substitute the `Rectangle` class.
* That is the violation of the LSP.  







