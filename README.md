# Paani Glass 💧

Chhoti si Android app (Java) — roz paani peene ki aadat banane ke liye. 3 pages:

1. **Main** — animated glass (lehar + bulbule). Glass ya button tap karo, ek glass add. "Wapas" se undo.
2. **History** — pichle 7 din ka bar chart, streak, average, kul litre.
3. **Settings** — daily goal (4–16 glass), glass size (150–500 ml), aaj ka reset, privacy note.

Sab design, icon aur code naya banaya gaya hai — kisi aur app ka copy nahi.

## Technical details (Play Store ke liye updated)
| Cheez | Value |
|---|---|
| compileSdk / targetSdk | **36** (Android 16) |
| minSdk | 26 (Android 8.0) |
| Android Gradle Plugin | 8.11.1 · Gradle 8.13 |
| Java | 17 |
| Permissions | **Koi nahi** |
| Internet / ads / tracking | Nahi |
| Edge-to-edge | Handle kiya hua (Android 15+ zaroori) |
| Native code | Nahi (16 KB page size ka masla nahi) |

## Tareeqa A: Bina Android Studio — GitHub par muft build (APK + AAB)
1. github.com par account banayein → **New repository** (Private) → is zip ka sara content upload karein (`.github` folder samait).
2. **Actions** tab kholein → "Build Paani Glass" khud chalega (5–8 minute).
3. Run khatam ho to neeche **Artifacts** se:
   - `PaaniGlass-test-apk` → zip kholein, `.apk` phone par install kar ke test karein.
   - `PaaniGlass-playstore-aab` → Play Store wali file (signing ke baad, neeche dekhein).

**AAB sign karna (Play Store ke liye zaroori):**
1. Keystore ek dafa banayein (kisi bhi PC jahan Java ho):
   `keytool -genkeypair -v -keystore upload.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload`
2. Use base64 mein badlein: `base64 -w0 upload.jks > upload.txt` (Windows: `certutil -encode upload.jks upload.txt`, phir pehli/aakhri line hata dein).
3. GitHub repo › **Settings › Secrets and variables › Actions** mein 4 secrets:
   `KEYSTORE_BASE64` (upload.txt ka text), `KEYSTORE_PASSWORD`, `KEY_ALIAS` (= upload), `KEY_PASSWORD`.
4. Actions › **Run workflow** dobara chalayein — ab AAB signed aayegi.
5. `upload.jks` aur passwords sambhal kar rakhein — gum ho gaye to update nahi kar sakenge.

## Tareeqa B: Android Studio mein kaise chalayein
1. Zip extract karein → Android Studio → **File › Open** → `PaaniGlass` folder.
2. Agar "Gradle wrapper missing" aaye to **Use Gradle wrapper / OK** dabayein — Studio khud download kar lega.
3. Sync ke baad ▶ Run.

## Play Store par upload
1. **Package name badlein** (zaroori, unique hona chahiye): `app/build.gradle.kts` mein `applicationId` aur `namespace`, aur `java/com/paaniglass/app` folder — Studio mein *Refactor › Rename* se.
2. **Build › Generate Signed App Bundle (.aab)** → apni keystore banayein (sambhal kar rakhein!).
3. Play Console mein:
   - **Privacy policy URL**: `PRIVACY_POLICY.md` ka text kisi free page (Google Sites / GitHub Pages) par daal kar link dein.
   - **Data safety**: "No data collected, no data shared".
   - **Ads**: No. **Target audience**: 13+ ya 18+ rakhein (bachon wali policy se bachne ke liye asaan).
   - **Health apps declaration**: "Not a medical app" — app sirf aadat tracker hai.
4. Screenshots aur feature graphic khud ke phone se lein.

`versionCode` har GitHub build par khud barhta hai — update ke liye bas code push karein aur nayi `.aab` upload karein.
