import PublicHeader from "./PublicHeader"
import HeroSection from "../public/HeroSection"
import UpcomingMatchesSection from "../public/UpcomingMatchesSection"
import AppShowcaseSection from "../public/AppShowcaseSection"
import PublicFooter from "./PublicFooter"

function PublicLayout() {
    return (
        <>
            <PublicHeader />
            <HeroSection />
            <UpcomingMatchesSection />
            <AppShowcaseSection />
            <PublicFooter />
        </>
    )
}

export default PublicLayout