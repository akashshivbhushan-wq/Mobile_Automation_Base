# York Laine App — Automation Test Cases

App under test: `YorkLaine8July.apk` (package `com.shopyorklaine.app`)
Source: locators and steps below are taken directly from the existing Eclipse project classes under
`automation/src/test/java/magenative/automation/`. Each test case references the exact class/method
and Appium locator already implemented, so this doubles as a traceability map between test cases and code.

Suite entry point: `Test1.java` (`testng.xml`). Run all: `mvn test -DapkPath=<path-to-apk>`.
Run one: filter `testng.xml` `<methods><include name="..."/></methods>`.

---

## TC-01 — Signup (new account)

**Class / Method:** `BaseSignUP.doCompleteSignup()` — driven by `Test1.signupOrLoginFlow()` (priority 1)
**Priority:** High
**Precondition:** Fresh app install, not logged in.

| Step | Action | Locator |
|---|---|---|
| 1 | Open side drawer | `By.xpath("//*[contains(@content-desc,'open') or @clickable='true']")` |
| 2 | Tap "Sign in" | `By.xpath("//*[contains(@resource-id,'signin') or ...text 'sign in']")` |
| 3 | Tap "Sign up" | `By.xpath("//*[contains(@resource-id,'signupbut') or ...text 'sign up']")` |
| 4 | Enter First Name | `By.xpath("//android.widget.EditText[contains(@resource-id,'firstname')]")` |
| 5 | Enter Last Name | `By.xpath("//android.widget.EditText[contains(@resource-id,'lastname')]")` |
| 6 | Enter Email | `By.xpath("//android.widget.EditText[contains(@resource-id,'email')]")` |
| 7 | Enter Password | `By.xpath("//android.widget.EditText[contains(@resource-id,'password') and not(contains(@resource-id,'Confirm'))]")` |
| 8 | Enter Confirm Password | `By.xpath("//android.widget.EditText[contains(@resource-id,'Confirm_password')]")` |
| 9 | Tap Register | `By.id(":id/MageNative_register")`, fallback `By.xpath("//android.widget.Button[contains(@text,'Create new account')]")` |

**Expected Result:** Either home page loads (`isSignupSuccess()` — locator `By.xpath("//*[contains(@resource-id,'home') or contains(@text,'Home')]")`), or a duplicate-email message appears (`isEmailAlreadyExists()` — `By.xpath("//*[contains(@text,'already') or contains(@text,'taken') or contains(@text,'used')]")`), in which case TC-02 (Login) should run instead.

**Test data:** First=`Amit`, Last=`Lodhi`, Email=`lodhi175@gmail.com`, Password=`Test@1234`

---

## TC-02 — Login (existing account)

**Class / Method:** `BaseLogin.doLoginFlow()` — driven by `Test1.signupOrLoginFlow()` fallback path
**Priority:** High
**Precondition:** Account already exists (email from TC-01 already registered).

| Step | Action | Locator |
|---|---|---|
| 1 | Skip if already logged in | `By.xpath("//*[contains(@resource-id,'home') or contains(@text,'Home')]")` |
| 2 | Open side drawer | `By.xpath("//*[contains(@content-desc,'open') or @clickable='true']")` |
| 3 | Tap Sign in | `By.xpath("//*[contains(@resource-id,'signin') or ...'sign in']")` |
| 4 | Enter email | `By.xpath("//android.widget.EditText[contains(@resource-id,'username')]")` |
| 5 | Enter password | `By.xpath("//android.widget.EditText[contains(@resource-id,'password') and not(contains(@resource-id,'Confirm'))]")` |
| 6 | Tap Login | `By.xpath("//android.widget.Button[contains(@resource-id,'login') or ...'sign in']")` |

**Expected Result:** Home page element becomes visible (`By.xpath("//*[contains(@resource-id,'home') or contains(@text,'Home')]")`).

**Test data:** Email=`lodhi175@gmail.com`, Password=`Test@1234`

---

## TC-03 — Account: Profile / Orders / Wishlist sections

**Class / Method:** `AccountPage.executeAccountFlow()` — driven by `Test1.accountFlowTest()` (priority 3)
**Priority:** Medium
**Precondition:** Logged in (TC-01/TC-02 passed).

| Step | Action | Locator |
|---|---|---|
| 1 | Open side drawer | `AppiumBy.accessibilityId("open")` |
| 2 | Open Account page | `By.xpath("//*[contains(@resource-id,'user_name')]")` |
| 3 | Verify username text | `By.xpath("//*[contains(@resource-id,'signin')]")` — assert non-empty |
| 4 | Open Orders | `By.xpath("//*[contains(@resource-id,'order')]")` |
| 5 | Verify Orders empty/non-empty state | `By.xpath("//*[contains(@resource-id,'nocarttext')]")` |
| 6 | Back to Account page | `AppiumBy.accessibilityId("Navigate up")` |
| 7 | Open Wishlist | `AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Wishlist\")")` |
| 8 | Verify Wishlist empty/non-empty state | `By.xpath("//*[contains(@resource-id,'nocarttext')]")` |
| 9 | Navigate back to Home (2x back) | `AppiumBy.accessibilityId("Navigate up")` |

**Expected Result:** Username displayed is non-empty; Orders and Wishlist sections each correctly report empty-state (`"No order placed"` / `"No Favourites"`) when the account has no history.

**Known gap:** current implementation only logs pass/fail, no `Assert` — a broken step won't fail the test. Recommend adding assertions if this test case needs to be a hard gate.

---

## TC-04 — Add to Wishlist, verify, remove, verify

**Class / Method:** `WishlistFlow.wishlistProductFlow()` — driven by `Test1.runWishlistFlow()` (priority 4)
**Priority:** High
**Precondition:** Logged in.

| Step | Action | Locator |
|---|---|---|
| 1 | Reset scroll to top of Home | `UiScrollable(...).scrollToBeginning(10)` |
| 2 | Click a product/card image | `UiSelector().description("image")` **or** resource-id fallback `//*[contains(@resource-id,':id/image') and not(ancestor::*[contains(@resource-id,'nav_view')])]` (York Laine has no `content-desc="image"` — always uses the fallback) |
| 3 | If listing (not PDP), click first product tile | Same image locator/fallback, scoped to listing page |
| 4 | Select variant if present | `//*[contains(@resource-id,'variant_name')]` |
| 5 | Tap wishlist (heart) button | `//*[contains(@resource-id,'wishdisable')]` |
| 6 | **Verify** wishlisted state toggled | `//*[contains(@resource-id,'wishenable')]` must now be present |
| 7 | Navigate to Wishlist page | Bottom-nav tab `UiSelector().textContains("Wishlist")` (falls back to `(//*[contains(@resource-id,'cart_icon')])[2]` for apps without a bottom nav) |
| 8 | **Verify** product appears in Wishlist | `//*[contains(@resource-id,'cancel_action')]` displayed |
| 9 | Tap remove (X) | `//*[contains(@resource-id,'cancel_action')]` |
| 10 | Confirm removal | `//*[contains(@resource-id,'ok_dialog')]` |
| 11 | **Verify** product removed | Remove-icon list empty OR empty-state `//*[contains(@resource-id,'nocarttext')]` shown |
| 12 | Back to Home (up to 3x) | `AppiumBy.accessibilityId("Navigate up")` |

**Expected Result:** All three `Assert.assertTrue` checks (steps 6, 8, 11) pass — this is a hard gate, not a soft log.

**Note:** If no wishlist button is found on the product (step 5), the flow **fails** unless the app first confirmed it actually reached a product/PDP page (`ProductPageTest.isOnProductPage()`) — this distinguishes "product genuinely has no wishlist option" (soft skip) from "navigation never worked" (hard failure).

---

## TC-05 — Sort listing A→Z, apply Availability filter

**Class / Method:** `SortingAndFilterpage` — driven by `Test1.runSortingAndFilterFlow()` (priority 5)
**Priority:** Medium
**Precondition:** Logged in, on Home page.

| Step | Action | Locator |
|---|---|---|
| 1 | Open listing via "View All" | `By.id("actiontext")` |
| 1b | Fallback: click banner/card image | `AppiumBy.accessibilityId("image")` or resource-id fallback `//*[contains(@resource-id,':id/image') and not(ancestor::*[contains(@resource-id,'nav_view')])]` |
| 2 | Open sort options | `By.id("sort_but")` |
| 3 | Select "A to Z" | `By.id("atoz")` |
| 4 | **Verify** product names now sorted A→Z | `By.xpath("//android.widget.TextView[contains(@resource-id,'name')]")` — text list compared case-insensitively against its own sorted copy |
| 5 | Open filter panel | `By.id("filter_icon")` |
| 6 | Open "Availability" filter (or first checkbox if not present) | `UiSelector().text("Availability")` → `(//android.widget.CheckBox)[1]`; fallback: any `//android.widget.CheckBox` |
| 7 | Apply filter | `By.id("btn_apply")` |
| 8 | **Verify** filtered products still present | `//android.widget.TextView[contains(@resource-id,'name')]` non-empty |
| 9 | Navigate back to Home (up to 3x) | `AppiumBy.accessibilityId("Navigate up")` |

**Expected Result:** Product list is genuinely alphabetically sorted; filtered result set is non-empty (or explicitly logged as empty, which is not currently asserted).

---

## TC-06 — Product Detail Page (PDP): image, name/price, variants, Add to Cart / Buy Now

**Class / Method:** `ProductPageTest.executeProductPageFlow()` — driven by `Test1.productPageFlowTest()` (priority 6)
**Priority:** High
**Precondition:** Logged in, on Home page.

| Step | Action | Locator |
|---|---|---|
| 1 | Click product from Home (with resource-id fallback) | `UiSelector().description("image").instance(0)` → fallback `//*[contains(@resource-id,':id/image') and not(ancestor::*[contains(@resource-id,'nav_view')])]` |
| 2 | Detect PDP vs listing landed on | `//*[contains(@resource-id,'addtocart')]` or `//*[contains(@resource-id,'buynow')]` present ⇒ PDP |
| 3 | If listing, click first product tile | Same image locator (+ fallback) scoped to listing |
| 4 | Verify product image displayed | `AppiumBy.accessibilityId("image")` — **note:** always fails on York Laine, no assertion attached (soft log only) |
| 5 | Verify name & price block displayed | `//*[contains(@resource-id,'quantitylayout')]` |
| 6 | Select variants if present | `//*[contains(@resource-id,'variant_name')]` |
| 7 | Verify Add to Cart button (or Out of Stock) | `//*[contains(@resource-id,'addtocart')]` / out-of-stock text `UiSelector().textContains("Out Of Stock")` |
| 8 | Verify Buy Now button | `//*[contains(@resource-id,'buynow')]` |
| 9 | Navigate back to Home | `driver.navigate().back()` (x1 or x2 depending on listing vs PDP) |

**Expected Result:** Name/price and both action buttons are displayed and enabled (unless out of stock).

**Known gap:** step 4 (`verifyProductImage`) will always log a failure on this app since it only checks `content-desc="image"` and never falls back — cosmetic-only, doesn't fail the test currently.

---

## TC-07 — Product Listing: scroll, verify card details, wishlist first item

**Class / Method:** `ProductListing.executeProductListingFlow()` — driven by `Test1.productListing()` (priority 7)
**Priority:** Medium
**Precondition:** Logged in, on Home page.

| Step | Action | Locator |
|---|---|---|
| 1 | Open listing via "View All" (fallback: banner/card image incl. resource-id fallback) | `By.id("actiontext")` → `AppiumBy.accessibilityId("image")` → `//*[contains(@resource-id,':id/image') and not(ancestor::*[contains(@resource-id,'nav_view')])]` |
| 2 | If "no products" message shown, go back and try an alternate listing | `By.id("nocarttext")`; alternate via 2nd `actiontext` match |
| 3 | Scroll to bottom | `UiScrollable(...).scrollToEnd(5)` |
| 4 | Scroll back up | `UiScrollable(...).scrollBackward()` |
| 5 | **Verify** first product name non-empty | `//android.widget.TextView[contains(@resource-id,'name')]` |
| 6 | **Verify** first product price non-empty | `//android.widget.TextView[contains(@resource-id,'specialprice')]` |
| 7 | Verify first product image displayed | `//android.widget.ImageView[contains(@resource-id,'image')]` |
| 8 | Tap wishlist icon on first product | `//android.widget.ImageView[contains(@resource-id,'wishlist_but')]` |
| 9 | Navigate back to Home | `AppiumBy.accessibilityId("Navigate up")` |

**Expected Result:** Name and price assertions (`Assert.assertFalse(..isEmpty())`) pass; wishlist icon click doesn't throw.

---

## TC-08 — Full Cart Flow: select in-stock product → variants → wishlist → add to cart → update qty → checkout

**Class / Method:** `CartPageTest.completeCartFlow()` — driven by `Test1.CartFlowTest()` (priority 8)
**Priority:** High
**Precondition:** Logged in, on Home page.

| Step | Action | Locator |
|---|---|---|
| 1 | Select a product, skipping out-of-stock ones (max 5 attempts) | `UiSelector().description("image").instance(0)`; stock check `//*[contains(@text,'Out Of Stock')]` |
| 2 | Select all available variants | `//*[contains(@resource-id,'variant_name')]` |
| 3 | Click wishlist icon on PDP | `<appPackage>:id/wishdisable` |
| 4 | Add to Cart | `<appPackage>:id/addtocart` |
| 5 | Go to Cart | `<appPackage>:id/cartsection` |
| 6 | Increase quantity | `<appPackage>:id/increase` |
| 7 | Decrease quantity (only if increase succeeded) | `<appPackage>:id/decrese` |
| 8 | Proceed to Checkout | `<appPackage>:id/proceedtocheck` |

**Expected Result:** Product added to cart, quantity updates, checkout reached.

**⚠️ Known gap — currently a false-positive risk:** step 1 uses the plain `content-desc="image"` locator **without** the resource-id fallback added elsewhere (`ProductPageTest`, `WishlistFlow`, `SortingAndFilterpage`, `ProductListing`). On York Laine this locator never matches, so step 1 fails immediately, every subsequent step logs "not found," and the test still reports **pass** because none of these steps use `Assert`. **Do not treat a green result for TC-08 as confirmation the cart flow actually works until this is fixed** (apply the same `productFromHomeFallback`-style locator + real assertions used in TC-04).

---

## Suggested execution order

Run in this order (matches `Test1.java` priorities) since later cases assume the logged-in state from TC-01/02:

1. TC-01 / TC-02 (signup or login)
2. TC-03 (account)
3. TC-04 (wishlist)
4. TC-05 (sort & filter)
5. TC-06 (PDP)
6. TC-07 (listing)
7. TC-08 (cart) — **fix known gap before trusting the result**

## Open issues to fix before these test cases are fully trustworthy

- **TC-08 (Cart):** no assertions, no resource-id locator fallback — currently can silently pass with zero real verification.
- **TC-03 (Account):** no assertions — a broken username/orders/wishlist check only prints a warning.
- **TC-06 step 4:** product-image check has no resource-id fallback (cosmetic only, not currently asserted).
