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
One of the lotto seller is just same with the previous case, but the new one(`DiscountedLottoSeller`)
sold a lotto for 500 price.

We can do like [this](OCPViolated.kt).

This case violates the OCP.  
The OCP states that "a module should be open for extension but closed for modification,"  
which means you should be able to add new functionality without altering existing code.

We can simply keep the OCP introducing interface [like this](OCPRefactored.kt).  

With 


